package com.paralect.topologies.replay;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Random;

public class DatabaseSpout  extends BaseRichSpout {
    SpoutOutputCollector _collector;
    Random _rand;
    int _counter;
    Object[] _events;

    public DatabaseSpout() {
        _events = new Object[] {
            createEvent(1, 100, "Tom", "tom@gmail.com"),
            createEvent(2, 200, "John", "john@gmail.com"),
            createEvent(1, 100, "Tom", "tom_new@gmail.com"),
            createEvent(2, 200, "John_new", "john_new@gmail.com"),
            createEvent(3, 300, "Bill", "bill@gmail.com"),
        };
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        _rand = new Random();
    }

    @Override
    public void nextTuple() {
        Utils.sleep(500);

        // pick random event
        Map<String, Object> event = (Map<String, Object>) _events[_rand.nextInt(_events.length)];

        // prepare tenantId and message JSON
        int tenantId = (int) event.get("tenantId");
        String message = mapToJson(event);

        // emit tuple [tenantId, message]
        _collector.emit(new Values(tenantId, message));
    }

    @Override
    public void ack(Object id) {
    }

    @Override
    public void fail(Object id) {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("tenantId", "message"));
    }

    private String mapToJson(Map<String, Object> map) {
        try {
            return new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "{}";
    }

    private Map<String, Object> createEvent(int tenantId, int userId, String userName, String userEmail) {
        return ImmutableMap.<String, Object>builder()
            .put("tenantId", tenantId)
            .put("userId", userId)
            .put("userName", userName)
            .put("email", userEmail)
            .build();
    }
}
