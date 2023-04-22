package com.talkhub.vertx.talkhub.router;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.services.TalkHubServices;
import com.talkhub.util.log.DebugLogger;

import io.vertx.ext.web.RoutingContext;

public class TalkHubNotificationRouter {
    public static void getAll(RoutingContext rc) {
        try {
            long userId = Long.parseLong(rc.request().headers().get("userid"));
            JsonObject res = TalkHubServices.talkHubNotificationService.getAll(userId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }
}
