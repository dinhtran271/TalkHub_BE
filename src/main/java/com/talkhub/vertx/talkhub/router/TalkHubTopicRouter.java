package com.talkhub.vertx.talkhub.router;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.services.TalkHubServices;
import com.talkhub.util.json.GsonUtil;
import com.talkhub.util.log.DebugLogger;

import io.vertx.ext.web.RoutingContext;

public class TalkHubTopicRouter {
    public static void create(RoutingContext rc) {
        try {
            long userId = Long.parseLong(rc.request().headers().get("userid"));
            String body = rc.body().asString();
            JsonObject data = GsonUtil.toJsonObject(body);
            data.addProperty("userid", userId);
            JsonObject res = TalkHubServices.talkHubTopicService.create(data);
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
            long topicId = Long.parseLong(rc.request().getParam("topicid"));
            JsonObject res = TalkHubServices.talkHubTopicService.delete(topicId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void getAllByUser(RoutingContext rc) {
        try {
            long userId = Long.parseLong(rc.request().getParam("userid"));
            JsonObject res = TalkHubServices.talkHubTopicService.getAllByUser(userId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void getMyTopic(RoutingContext rc) {
        try {
            long userId = Long.parseLong(rc.request().headers().get("userid"));
            JsonObject res = TalkHubServices.talkHubTopicService.getAllByUser(userId);
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
            long userId = Long.parseLong(rc.request().headers().get("userid"));
            String body = rc.body().asString();
            JsonObject data = GsonUtil.toJsonObject(body);
            data.addProperty("userid", userId);
            JsonObject res = TalkHubServices.talkHubTopicService.update(userId, data);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void updateLike(RoutingContext rc) {
        try {
            String body = rc.body().asString();
            JsonObject json = GsonUtil.toJsonObject(body);
            long topicId = json.get("topicid").getAsLong();
            JsonObject res = TalkHubServices.talkHubTopicService.updateLike(topicId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void getById(RoutingContext rc) {
        try {
            long topicId = Long.parseLong(rc.request().getParam("topicid"));
            JsonObject res = TalkHubServices.talkHubTopicService.getById(topicId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void updateView(RoutingContext rc) {
        try {
            String body = rc.body().asString();
            JsonObject json = GsonUtil.toJsonObject(body);
            long topicId = json.get("topicid").getAsLong();
            JsonObject res = TalkHubServices.talkHubTopicService.updateView(topicId);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void getAllByCategory(RoutingContext rc) {
        try {
            long categoryId = Long.parseLong(rc.request().getParam("categoryid"));
            int page = Integer.parseInt(rc.request().getParam("page"));
            int count = Integer.parseInt(rc.request().getParam("count"));
            JsonObject res = TalkHubServices.talkHubTopicService.getAllByCategory(categoryId, page, count);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }

    public static void getAllByTag(RoutingContext rc) {
        try {
            String tag = rc.request().getParam("tag");
            int page = Integer.parseInt(rc.request().getParam("page"));
            int count = Integer.parseInt(rc.request().getParam("count"));
            JsonObject res = TalkHubServices.talkHubTopicService.getAllByTag(tag, page, count);
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
            int page = Integer.parseInt(rc.request().getParam("page"));
            int count = Integer.parseInt(rc.request().getParam("count"));
            JsonObject res = TalkHubServices.talkHubTopicService.getAll(page, count);
            rc.response().end(res.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject res = BaseResponse.createFullMessageResponse(1, "system_error");
            rc.response().end(res.toString());
        }
    }
}
