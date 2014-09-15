var Bolt = require('../storm-node-multilang').Bolt;

//The Bolt constructor takes a definition function,
//an input stream and an output stream
new Bolt(function(events) {
    var collector = null;

    //You can listen to the bolt "prepare" event to
    //fetch a reference to the OutputCollector instance
    events.on('prepare', function(c) {
        collector = c;
    });

    //The definition function must return a function used as
    //the execute function.
    return function(tuple, cb) {

        // calculate square
        var square = tuple.values[0] * tuple.values[0];

        collector.emit([square]);
        collector.ack(tuple);
        cb();
    }

}, process.stdin, process.stdout);

process.stdin.setEncoding('utf8');
process.stdin.resume();