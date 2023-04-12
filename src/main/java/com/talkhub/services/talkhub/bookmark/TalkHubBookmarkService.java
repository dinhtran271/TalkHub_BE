package com.talkhub.services.talkhub.bookmark;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.util.log.DebugLogger;
import com.talkhub.util.sql.HikariClients;
import com.talkhub.util.sql.SQLJavaBridge;

public class TalkHubBookmarkService implements ITalkHubBookmarkService{

    @Override
    public JsonObject create(JsonObject data) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            long userId = data.get("userid").getAsLong();
            long topicId = data.get("topicid").getAsLong();
            String query = "INSERT INTO bookmark(userid, topicid) VALUES (?,?)";
            bridge.update(query, userId, topicId);
            return BaseResponse.createFullMessageResponse(0, "success");
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error", data);
        }
    }

    @Override
    public JsonObject delete(long userId, long topicId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "DELETE FROM bookmark WHERE userid = ? and topicid = ?";
            bridge.update(query, userId, topicId);
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
            String query = "SELECT * FROM bookmark WHERE userid=?";
            JsonArray marked = bridge.query(query, userId);
            JsonObject data = new JsonObject();
            data.add("marked", marked);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

}
