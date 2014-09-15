package com.paralect.topologies.simple;

import backtype.storm.task.ShellBolt;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;

import java.util.Map;

public class IncreaseBolt extends ShellBolt implements IRichBolt {

    public IncreaseBolt(int delta) {
        // Example of passing params to Node.js bolts
        super("node", "simple-topology/increase.js", String.valueOf(delta));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("result"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
