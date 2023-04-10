package com.talkhub.vertx.talkhub.router;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.services.TalkHubServices;
import com.talkhub.util.json.GsonUtil;
import com.talkhub.util.log.DebugLogger;

import io.vertx.ext.web.RoutingContext;

public class TalkHubFollowRouter {
    public static void create(RoutingContext rc) {
        try {
            String body = rc.body().asString();
            JsonObject data = GsonUtil.toJsonObject(body);
            JsonObject res = TalkHubServices.talkHubFollowService.create(data);
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
            long otherId = Long.parseLong(rc.request().getParam("otherid"));
            JsonObject res = TalkHubServices.talkHubFollowService.delete(userId, otherId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void getAllFollowed(RoutingContext rc) {
        try {
            long userId = Long.parseLong(rc.request().headers().get("userid"));
            JsonObject res = TalkHubServices.talkHubFollowService.getAllFollowed(userId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void getAllFollower(RoutingContext rc) {
        try {
            long otherId = Long.parseLong(rc.request().getParam("otherid"));
            JsonObject res = TalkHubServices.talkHubFollowService.getAllFollowed(otherId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void countFollowed(RoutingContext rc) {
        try {
            long userId = Long.parseLong(rc.request().headers().get("userid"));
            JsonObject res = TalkHubServices.talkHubFollowService.countFollowed(userId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void countFollower(RoutingContext rc) {
        try {
            long otherId = Long.parseLong(rc.request().getParam("otherid"));
            JsonObject res = TalkHubServices.talkHubFollowService.countFollower(otherId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }
}
