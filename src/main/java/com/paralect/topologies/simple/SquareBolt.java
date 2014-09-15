package com.paralect.topologies.simple;

import backtype.storm.task.ShellBolt;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;

import java.util.Map;

public class SquareBolt extends ShellBolt implements IRichBolt {

    public SquareBolt() {
        super("node", "simple-topology/square.js");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("square"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
