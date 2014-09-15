package com.paralect.topologies.processor;

import backtype.storm.task.ShellBolt;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;

import java.util.Map;

public class ValidatorBolt extends ShellBolt implements IRichBolt {

    public ValidatorBolt() {
        super("node", "processor-topology/validator.js");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("messageId", "userId", "userEmail", "userName"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
