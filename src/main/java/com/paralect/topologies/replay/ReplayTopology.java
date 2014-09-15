package com.paralect.topologies.replay;

import backtype.storm.Config;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.paralect.utils.StormRunner;

public class ReplayTopology {
    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("database", new DatabaseSpout());

        // ReplayBolt will receive tuples from "database" spout, partitioned by tenantId
        builder.setBolt("replay", new ReplayBolt(), 3)
                .fieldsGrouping("database", new Fields("tenantId"));

        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(1);
        conf.setMaxTaskParallelism(3);

        if (args != null && args.length > 0) {
            StormRunner.runTopologyRemotely(args[0], conf, builder.createTopology());
        } else {
            StormRunner.runTopologyLocally("replay-topology", conf, builder.createTopology(), 10);
        }
    }
}
