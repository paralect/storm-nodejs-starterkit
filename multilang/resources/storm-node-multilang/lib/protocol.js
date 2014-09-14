'use strict';
var fs = require('fs');
var util = require('util');
var EventEmitter = require('events').EventEmitter;

var Protocol = module.exports.MultilangProtocol = function(inputStream, outputStream) {

	this._inputStream = inputStream || process.stdin;
	this._outputStream = outputStream || process.stdout;
	this._msgs = [];
	this._heartbeatDir = null;
	this._stormConf = null;
	this._topologyContext = null;

	this._readMessages();

	EventEmitter.call(this);
};

util.inherits(Protocol, EventEmitter);

Protocol.prototype._readMessages = function() {
	var self = this;

	this._inputStream.on('data', function(chunk) {

		if (chunk instanceof Buffer) {
			chunk = chunk.toString();
		}

		var chunks = chunk.split("\n");
		var last_end = 0;

		self._msgs = self._msgs.concat(chunks)

		for(var i in self._msgs) {
			if(self._msgs[i] == "end") {
				self._onMessage(self._msgs.slice(last_end, i).join("\n").trim());
				last_end = parseInt(i) + 1;
			}
		}

		self._msgs.splice(0, last_end);
	});

};

Protocol.prototype._onMessage = function(message) {
    var messageObject;

	if (!message) {
        this.sendLog('no message'); return;
    }

	//after heartbeatDir received, messages are JSON encoded
	try {
		messageObject = JSON.parse(message);
	} catch(e) {
		this.emit('error', e);
		return;
	}

	if (!this._heartbeatDir) {
		this._heartbeatDir = messageObject.pidDir;
        this._stormConf = messageObject.conf;
        this._topologyContext = messageObject.context;
		this._sendPid(this._heartbeatDir);
	} else {
        // this.sendLog('\n\n\ngot message: ' + message + '\n\n\n');
		this.emit('message', messageObject);
	}


};




Protocol.prototype._sendPid = function(heartbeatDir) {
	var self = this,
        pid = process.pid;

	fs.open(heartbeatDir + '/' + pid, 'w', function(err, fd) {
		if (!err) {
			fs.close(fd);
		}

        self.emit('ready');

        self.sendMessage({
            pid: pid
        });
	});
}


Protocol.prototype.sendMessage = function(message) {

	var messageString = JSON.stringify(message);

	this._outputStream.write(messageString + "\nend\n");
};

Protocol.prototype.sendLog = function(message) {
	this.sendMessage({
		command:'log',
		msg:message
	});
};


Protocol.prototype.sendSync = function() {

	this.sendMessage({ command: 'sync' });
};



Protocol.prototype.getStormConf = function() {
	return this._stormConf;
};

Protocol.prototype.getTopologyContext = function() {
	return this._topologyContext;

};

Protocol.prototype.emitTuple = function(tuple, stream, anchors, directTask) {
    var context;

	if (!anchors) anchors = [];

	var message = {
		command:'emit'
	}

	if(stream) {
		message.stream = stream;
	}

	if(directTask) {
        context = this.getTopologyContext();

        for(var i in context) {
            if(context.hasOwnProperty(i) && context[i] === directTask) {
                directTask = parseInt(i, 10);
            }
        }

		message.task = directTask;
	}

	message.anchors = anchors.map(function(anchor) {
		return anchor.id;
	})

	message.tuple = tuple;

	this.sendMessage(message);

}