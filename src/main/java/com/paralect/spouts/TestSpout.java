package com.paralect.spouts;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.Map;
import java.util.Random;

public class TestSpout extends BaseRichSpout {
    SpoutOutputCollector _collector;
    Random _rand;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        _rand = new Random();
    }

    @Override
    public void nextTuple() {
        Utils.sleep(500);

        String[] jsons = new String[] {
                "{ \"key\" : \"one\", \"value\" : \"one\" }",
                "{ \"key\" : \"two\", \"value\" : \"two\" }",
                "{ \"key\" : \"three\", \"value\" : \"three\" }",
                "{ \"key\" : \"four\", \"value\" : \"four\" }",
                "{ \"key\" : \"five\", \"value\" : \"five\" }",
        };

        String sentence = jsons[_rand.nextInt(jsons.length)];
        _collector.emit(new Values(sentence));
    }

    @Override
    public void ack(Object id) {
    }

    @Override
    public void fail(Object id) {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("json"));
    }
}
