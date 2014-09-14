package com.paralect;

import backtype.storm.Config;
import backtype.storm.topology.TopologyBuilder;
import com.paralect.bolts.NodeBolt;
import com.paralect.spouts.TestSpout;
import com.paralect.utils.StormRunner;

/**
 * Run locally with this command:
 * mvn compile exec:java -Dstorm.topology=com.paralect.TestTopology
 */
public class TestTopology {
    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout", new TestSpout(), 1);
        builder.setBolt("node-bolt", new NodeBolt(), 1).shuffleGrouping("spout");

        Config conf = new Config();
        conf.setDebug(true);

        if (args != null && args.length > 0) {
            conf.setNumWorkers(1);
            conf.setMaxTaskParallelism(3);
            StormRunner.runTopologyRemotely(args[0], conf, builder.createTopology());
        } else {
            conf.setMaxTaskParallelism(1);
            StormRunner.runTopologyLocally("test-topology", conf, builder.createTopology(), 10);
        }
    }
}
