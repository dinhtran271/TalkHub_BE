package com.talkhub.services.talkhub.follow;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.util.log.DebugLogger;
import com.talkhub.util.sql.HikariClients;
import com.talkhub.util.sql.SQLJavaBridge;

public class TalkHubFollowService implements ITalkHubFollowService{

    @Override
    public JsonObject create(JsonObject data) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            long userId = data.get("userid").getAsLong();
            long otherId = data.get("otherid").getAsLong();
            String query = "INSERT INTO follow(userid, otherid) VALUES (?,?)";
            bridge.update(query, userId, otherId);
            return BaseResponse.createFullMessageResponse(0, "success");
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error", data);
        }
    }

    @Override
    public JsonObject delete(long userId, long otherId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "DELETE FROM follow WHERE userid = ? and otherid = ?";
            bridge.update(query, userId, otherId);
            return BaseResponse.createFullMessageResponse(0, "success");
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getAllFollowed(long userId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM follow WHERE userid=?";
            JsonArray followed = bridge.query(query, userId);
            JsonObject data = new JsonObject();
            data.add("followed", followed);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject countFollowed(long userId) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT COUNT(otherid) AS followed FROM follow WHERE userid=?";
            JsonObject data = bridge.queryOne(query, userId);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject countFollower(long otherId) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT COUNT(userid) AS followed FROM follow WHERE otherid=?";
            JsonObject data = bridge.queryOne(query, otherId);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getAllFollower(long otherId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM follow WHERE otherid=?";
            JsonArray follower = bridge.query(query, otherId);
            JsonObject data = new JsonObject();
            data.add("follower", follower);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }


}
