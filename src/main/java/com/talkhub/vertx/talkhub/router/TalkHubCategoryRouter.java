package com.talkhub.vertx.talkhub.router;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.services.TalkHubServices;
import com.talkhub.util.json.GsonUtil;
import com.talkhub.util.log.DebugLogger;

import io.vertx.ext.web.RoutingContext;

public class TalkHubCategoryRouter {
    public static void create(RoutingContext rc) {
        try {
            String body = rc.body().asString();
            JsonObject data = GsonUtil.toJsonObject(body);
            JsonObject res = TalkHubServices.talkHubCategoryService.create(data);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void getAll(RoutingContext rc) {
        try {
            JsonObject res = TalkHubServices.talkHubCategoryService.getAll();
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void update(RoutingContext rc) {
        try {
            String body = rc.body().asString();
            JsonObject data = GsonUtil.toJsonObject(body);
            JsonObject res = TalkHubServices.talkHubCategoryService.update(data);
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
            long cateId = Long.parseLong(rc.request().getParam("categoryid"));
            JsonObject res = TalkHubServices.talkHubCategoryService.delete(cateId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

}
