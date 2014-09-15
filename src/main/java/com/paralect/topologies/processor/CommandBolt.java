package com.paralect.topologies.processor;

import backtype.storm.task.ShellBolt;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;

import java.util.Map;

public class CommandBolt extends ShellBolt implements IRichBolt {

    public CommandBolt() {
        super("node", "processor-topology/commands.js");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("tenantId", "message"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
