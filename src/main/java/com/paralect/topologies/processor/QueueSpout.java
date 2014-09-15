package com.paralect.topologies.processor;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class QueueSpout extends BaseRichSpout {
    SpoutOutputCollector _collector;
    Random _rand;
    int _counter;
    Object[] _messages;

    public QueueSpout() {
        _messages = new Object[] {
            createChangeUserSettingsMap(1, 100, "Tom", "tom@gmail.com"),
            createChangeUserSettingsMap(2, 200, "John", "john@gmail.com"),
            createChangeUserSettingsMap(1, 100, "Tom", "tom_new@gmail.com"),
            createChangeUserSettingsMap(2, 200, "John_new", "john_new@gmail.com"),
            createChangeUserSettingsMap(3, 300, "Bill", "bill@gmail.com"),
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

        // pick random message
        Map<String, Object> message = (Map<String, Object>) _messages[_rand.nextInt(_messages.length)];

        // prepare fields
        int tenantId = (int) message.get("tenantId");
        String json = mapToJson(message);

        // emits tuple [tenantId, message]
        _collector.emit(new Values(tenantId, json));
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

    private Map<String, Object> createChangeUserSettingsMap(int tenantId, int userId, String userName, String userEmail) {
        return ImmutableMap.<String, Object>builder()
            .put("messageType", "ChangeUserSettings")
            .put("tenantId", tenantId)
            .put("userId", userId)
            .put("userName", userName)
            .put("userEmail", userEmail)
            .build();
    }
}
