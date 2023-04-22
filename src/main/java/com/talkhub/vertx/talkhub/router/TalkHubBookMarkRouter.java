package com.talkhub.vertx.talkhub.router;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.services.TalkHubServices;
import com.talkhub.util.json.GsonUtil;
import com.talkhub.util.log.DebugLogger;

import io.vertx.ext.web.RoutingContext;

public class TalkHubBookMarkRouter {
    public static void create(RoutingContext rc) {
        try {
            long userId = Long.parseLong(rc.request().headers().get("userid"));
            String body = rc.body().asString();
            JsonObject data = GsonUtil.toJsonObject(body);
            data.addProperty("userid", userId);
            JsonObject res = TalkHubServices.talkHubBookmarkService.create(data);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void delete(RoutingContext rc) {
        try {
            long userId = Long.parseLong(rc.request().headers().get("userid"));
            long topicId = Long.parseLong(rc.request().getParam("topicid"));
            JsonObject res = TalkHubServices.talkHubBookmarkService.delete(userId, topicId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }
    public static void getAllTopicBookmark(RoutingContext rc) {
        try {
            long userId = Long.parseLong(rc.request().headers().get("userid"));
            JsonObject res = TalkHubServices.talkHubBookmarkService.getAllTopicBookmark(userId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }
}
