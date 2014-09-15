#!/bin/sh
# Submit topology to the cluster
mvn package -Dstorm.lib.scope=provided
storm jar target/storm-nodejs-starterkit-1.0-SNAPSHOT-jar-with-dependencies.jar com.paralect.topologies.simple.SimpleTopology simple-topology
