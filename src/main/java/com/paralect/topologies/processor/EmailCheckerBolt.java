package com.paralect.topologies.processor;

import backtype.storm.task.ShellBolt;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;

import java.util.Map;

public class EmailCheckerBolt  extends ShellBolt implements IRichBolt {

    public EmailCheckerBolt() {
        super("node", "processor-topology/email-checker.js");
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("messageType", "userId", "available"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
