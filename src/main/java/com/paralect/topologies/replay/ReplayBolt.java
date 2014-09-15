package com.paralect.topologies.replay;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.ShellBolt;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;

import java.util.Map;

public class ReplayBolt  extends ShellBolt implements IRichBolt {

    public ReplayBolt() {
        super("node", "replay-topology/replay.js");
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
