package com.paralect.topologies.simple;

import backtype.storm.Config;
import backtype.storm.topology.TopologyBuilder;
import com.paralect.utils.StormRunner;

public class SimpleTopology {
    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("counter", new CounterSpout());
        builder.setBolt("square", new SquareBolt())
            .shuffleGrouping("counter");
        builder.setBolt("increase", new IncreaseBolt(10))
            .shuffleGrouping("square");

        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(1);
        conf.setMaxTaskParallelism(1);

        if (args != null && args.length > 0) {
            StormRunner.runTopologyRemotely(args[0], conf, builder.createTopology());
        } else {
            StormRunner.runTopologyLocally("simple-topology", conf, builder.createTopology(), 10);
        }
    }
}

