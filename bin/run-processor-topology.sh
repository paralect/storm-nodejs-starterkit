#!/bin/sh
# Run topology locally
mvn compile exec:java -Dstorm.topology=com.paralect.topologies.processor.ProcessorTopology
