package com.talkhub.services.talkhub.post;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.services.TalkHubServices;
import com.talkhub.util.log.DebugLogger;
import com.talkhub.util.sql.HikariClients;
import com.talkhub.util.sql.SQLJavaBridge;

public class TalkHubPostService implements ITalkHubPostService{

    @Override
    public JsonObject create(long userId, JsonObject data) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            data.addProperty("userid", userId);
            data.addProperty("create_time", System.currentTimeMillis());
            data.addProperty("likes", 0);
            bridge.insertObjectToDB("th_post", data);
            long topicId = data.get("topicid").getAsLong();
            long authorId = TalkHubServices.talkHubTopicService.getUserId(topicId);
            if (authorId != -1)
                TalkHubServices.talkHubNotificationService.create(authorId, userId, topicId, System.currentTimeMillis());
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject updateLike(long postId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "UPDATE th_post set likes=likes+? WHERE id=?";
            bridge.update(query, 1, postId);
            return BaseResponse.createFullMessageResponse(0, "success");
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject delete(long postId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "DELETE FROM th_post WHERE id=?";
            bridge.update(query, postId);
            return BaseResponse.createFullMessageResponse(0, "success");
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getAllByTopic(long topicId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM th_post WHERE topicid=?";
            JsonArray topics = bridge.query(query, topicId);
            for (int i =0 ; i<topics.size(); i++) {
                JsonObject post = topics.get(i).getAsJsonObject();
                long userId = post.get("userid").getAsLong();
                JsonObject profile = TalkHubServices.talkHubProfileService.getInfo(userId).get("data").getAsJsonObject();
                String postUserName = profile.get("nick_name").getAsString();
                String postUserAvatar = profile.get("avatar").getAsString();
                post.addProperty("post_user_name", postUserName);
                post.addProperty("post_user_avatar", postUserAvatar);
            }
            JsonObject data = new JsonObject();
            data.add("posts", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
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
            String query = "SELECT * FROM th_post WHERE userid=?";
            JsonArray topics = bridge.query(query, userId);
            JsonObject data = new JsonObject();
            data.add("posts", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject update(JsonObject data) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            bridge.updateObjectToDb("th_post", data);
            return BaseResponse.createFullMessageResponse(0, "success",data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public long countPostOfTopic(long topcicId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT COALESCE(COUNT(id),0) FROM th_post WHERE topicid=?";
            long amount = bridge.queryLong(query, topcicId);
            return amount;
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return 0;
        }
    }

}
