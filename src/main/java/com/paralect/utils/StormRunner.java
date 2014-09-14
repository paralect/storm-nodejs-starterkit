package com.paralect.utils;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;

public final class StormRunner {

    private static final int MILLIS_IN_SEC = 1000;

    private StormRunner() {
    }

    public static void runTopologyLocally(String topologyName, Config conf, StormTopology topology, int runtimeInSeconds)
            throws InterruptedException {
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(topologyName, conf, topology);
        Thread.sleep((long) runtimeInSeconds * MILLIS_IN_SEC);
        cluster.killTopology(topologyName);
        cluster.shutdown();
    }

    public static void runTopologyRemotely(String topologyName, Config conf, StormTopology topology)
            throws AlreadyAliveException, InvalidTopologyException {
        StormSubmitter.submitTopologyWithProgressBar(topologyName, conf, topology);
    }
}