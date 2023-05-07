package com.talkhub.services.talkhub.profile;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.util.log.DebugLogger;
import com.talkhub.util.sql.HikariClients;
import com.talkhub.util.sql.SQLJavaBridge;

public class TalkHubProfileService implements ITalkHubProfileService{

    public String randomAvatar() {
        JsonObject url= new JsonObject();
        url.addProperty("1", "https://i.ibb.co/m8DLwd4/1.jpg");
        url.addProperty("2", "https://i.ibb.co/jWkxRqk/2.jpg");
        url.addProperty("3", "https://i.ibb.co/zVvLHwK/3.jpg");
        url.addProperty("4", "https://i.ibb.co/SNW2ZkJ/4.jpg");
        url.addProperty("5", "https://i.ibb.co/PGs7LTg/5.jpg");
        url.addProperty("6", "https://i.ibb.co/dt5ycPc/6.jpg");
        url.addProperty("7", "https://i.ibb.co/XDdJ7H0/7.jpg");
        url.addProperty("8", "https://i.ibb.co/9TcNP80/8.jpg");
        url.addProperty("9", "https://i.ibb.co/Pcy1PbG/9.jpg");
        url.addProperty("10", "https://i.ibb.co/t2FX7rH/10.jpg");

        int i = RandomUtils.nextInt(1, 11);
        return url.get("" + i).getAsString();
    }

    @Override
    public JsonObject create(long userId, JsonObject data) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            data.addProperty("userid", userId);
            String query = "SELECT * FROM th_profile WHERE userid = ?";
            boolean isExist = bridge.queryExist(query, userId);
            if (isExist)
                update(data);
            data.addProperty("likes", 0);
            bridge.insertObjectToDB("th_profile","userid", data);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject create(long userId, String nickName) {
        try {
            String avatar = randomAvatar();
            JsonObject profile = new JsonObject();
            profile.addProperty("userid", userId);
            profile.addProperty("nick_name", nickName);
            profile.addProperty("email", "");
            profile.addProperty("avatar", avatar);
            profile.addProperty("age", 0);
            return create(userId, profile);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getInfo(long userId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM th_profile WHERE userid = ?";
            boolean isExist = bridge.queryExist(query, userId);
            if (!isExist)
                create(userId, "user@0" + userId);
            JsonObject data = bridge.queryOne(query, userId);
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
            data.addProperty("likes", 0);
            bridge.updateObjectToDb("th_profile", "userid", data);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

}
