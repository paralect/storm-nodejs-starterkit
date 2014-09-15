var Bolt = require('../storm-node-multilang').Bolt;

new Bolt(function(events) {
    var collector = null;
    var context = null;

    events.on('prepare', function(coll, cont) {
        collector = coll;
        context = cont;
    });

    return function(tuple, cb) {

        var message = JSON.parse(tuple.values[1]);

        // execute command here, and generate event
        var event = {
            tenantId : message.tenantId,
            newEmail : message.userEmail
        }

        collector.emit([message.tenantId, JSON.stringify(event)]);
        collector.ack(tuple);
        cb();
    }

}, process.stdin, process.stdout);

process.stdin.setEncoding('utf8');
process.stdin.resume();