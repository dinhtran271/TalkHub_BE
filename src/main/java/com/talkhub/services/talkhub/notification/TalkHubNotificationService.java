package com.talkhub.services.talkhub.notification;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.services.TalkHubServices;
import com.talkhub.util.log.DebugLogger;
import com.talkhub.util.sql.HikariClients;
import com.talkhub.util.sql.SQLJavaBridge;

public class TalkHubNotificationService implements ITalkHubNotificationService{
    @Override
    public JsonObject create(long userid, long otherid, long topcId, long createTime) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            JsonObject data = new JsonObject();
            data.addProperty("userid", userid);
            data.addProperty("otherid", otherid);
            data.addProperty("topicid", topcId);
            data.addProperty("create_time", createTime);
            bridge.insertObjectToDB("th_notification", data);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getAll(long userId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM th_notification WHERE userid=?";
            JsonArray notis = bridge.query(query, userId);
            for (int i=0; i<notis.size(); i++) {
                JsonObject noti = notis.get(i).getAsJsonObject();
                long otherId = noti.get("otherid").getAsLong();
                long topicId = noti.get("topicid").getAsLong();
                JsonObject profile = TalkHubServices.talkHubProfileService.getInfo(otherId).get("data").getAsJsonObject();
                noti.addProperty("other_name", profile.get("nick_name").getAsString());
                noti.addProperty("other_avatar", profile.get("avatar").getAsString());
                JsonObject topic = TalkHubServices.talkHubTopicService.getById(topicId, userId).get("data").getAsJsonObject();
                noti.addProperty("topic_title", topic.get("title").getAsString());
            }
            JsonObject data = new JsonObject();
            data.add("noti", notis);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }
}
