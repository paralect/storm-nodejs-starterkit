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
            createChangeUserSettingsMap(1, "Tom", "tom@gmail.com"),
            createChangeUserSettingsMap(2, "John", "john@gmail.com"),
            createChangeUserSettingsMap(1, "Tom", "tom_new@gmail.com"),
            createChangeUserSettingsMap(2, "John_new", "john_new@gmail.com"),
            createChangeUserSettingsMap(3, "Bill", "bill@gmail.com"),
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
        int userId = (int) message.get("userId");
        String messageId = (String) message.get("messageId");
        String messageType = (String) message.get("messageType");
        String json = "";

        try {
            json = mapToJson(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        _collector.emit(new Values(messageType, messageId, userId, json)); // emits tuple [messageType, messageId, userId, json]
    }

    @Override
    public void ack(Object id) {
    }

    @Override
    public void fail(Object id) {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("messageType", "messageId", "userId", "message"));
    }


    private String mapToJson(Map<String, Object> map) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(map);
    }

    private Map<String, Object> createChangeUserSettingsMap(int userId, String userName, String userEmail) {
        return ImmutableMap.<String, Object>builder()
            .put("messageType", "ChangeUserSettings")
            .put("messageId", _counter++)
            .put("userId", userId)
            .put("userName", userName)
            .put("email", userEmail)
            .build();
    }
}
