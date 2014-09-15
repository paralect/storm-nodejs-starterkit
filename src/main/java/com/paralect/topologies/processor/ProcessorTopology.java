package com.paralect.topologies.processor;

import backtype.storm.Config;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.paralect.topologies.simple.CounterSpout;
import com.paralect.utils.StormRunner;

public class ProcessorTopology {
    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("queue", new QueueSpout());
        builder.setBolt("validator", new ValidatorBolt())
                .shuffleGrouping("queue");
        builder.setBolt("email-checker", new EmailCheckerBolt())
                .fieldsGrouping("validator", new Fields("userEmail"));

        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(1);
        conf.setMaxTaskParallelism(1);

        if (args != null && args.length > 0) {
            StormRunner.runTopologyRemotely(args[0], conf, builder.createTopology());
        } else {
            StormRunner.runTopologyLocally("processor-topology", conf, builder.createTopology(), 10);
        }
    }
}
