package com.paralect.topologies.processor;

import backtype.storm.task.ShellBolt;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;

import java.util.Map;

public class EventsBolt extends ShellBolt implements IRichBolt {

    public EventsBolt() {
        super("node", "processor-topology/events.js");
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

