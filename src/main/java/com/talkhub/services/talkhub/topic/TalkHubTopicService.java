package com.talkhub.services.talkhub.topic;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.util.log.DebugLogger;
import com.talkhub.util.sql.HikariClients;
import com.talkhub.util.sql.SQLJavaBridge;


public class TalkHubTopicService implements ITalkHubTopicService{

    @Override
    public JsonObject create(JsonObject data) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            data.addProperty("create_time", System.currentTimeMillis());
            data.addProperty("likes", 0);
            data.addProperty("view", 0);
            bridge.insertObjectToDB("th_topic", data);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject delete(long topicId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "DELETE FROM th_topic WHERE id=?";
            bridge.update(query, topicId);
            return BaseResponse.createFullMessageResponse(0, "success");
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getAllByUser(long userId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM th_topic WHERE userid=?";
            JsonArray topics = bridge.query(query, userId);
            JsonObject data = new JsonObject();
            data.add("topics", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject update(long userId, JsonObject data) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            long topicId = data.get("id").getAsLong();
            String title = data.get("title").getAsString();
            JsonObject content = data.get("content").getAsJsonObject();
            JsonObject topic = new JsonObject();
            topic.addProperty("id", topicId);
            topic.addProperty("userid", userId);
            topic.addProperty("title", title);
            topic.add("content",  content);
            bridge.updateObjectToDb("th_topic", topic);
            return BaseResponse.createFullMessageResponse(0, "success", topic);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject updateLike(long topicId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "UPDATE th_topic set likes=? WHERE id=?";
            bridge.update(query, 1, topicId);
            return BaseResponse.createFullMessageResponse(0, "success");
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getById(long topicId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM th_topic WHERE id=?";
            JsonArray topics = bridge.query(query, topicId);
            JsonObject data = new JsonObject();
            data.add("topics", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject updateView(long topicId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "UPDATE th_topic set view=? WHERE id=?";
            bridge.update(query, 1, topicId);
            return BaseResponse.createFullMessageResponse(0, "success");
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getAll(int page, int count) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM th_topic LIMIT ? OFFSET ?";
            JsonArray topics = bridge.query(query, count, (page-1)*count);
            JsonObject data = new JsonObject();
            data.add("topics", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getAllByCategory(long categoryId, int page, int count) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM th_topic WHERE categoryid=? LIMIT ? OFFSET ?";
            JsonArray topics = bridge.query(query, categoryId, count, (page-1)*count);
            JsonObject data = new JsonObject();
            data.add("topics", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getAllByTag(String tagId, int page, int count) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT th_topic.*, th_tag.id, th_tag.name FROM th_topic, th_tag WHERE " + 
            "th_tag.id=? LIMIT ? OFFSET ?";
            JsonArray topics = bridge.query(query, tagId, count, (page-1)*count);
            JsonObject data = new JsonObject();
            data.add("topics", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }
    
    
}
