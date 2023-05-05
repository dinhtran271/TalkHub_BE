package com.talkhub.vertx.talkhub.router;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.services.TalkHubServices;
import com.talkhub.util.json.GsonUtil;
import com.talkhub.util.log.DebugLogger;

import io.vertx.ext.web.RoutingContext;

public class TalkHubAuthRouter {
    public static void authorizeUser(RoutingContext rc) {
        try {
            long userId = Long.parseLong(rc.request().getHeader("userid"));
            String token = rc.request().getHeader("token");
            JsonObject response = TalkHubServices.talkHubAuthService.authorize(userId, token);
            if (BaseResponse.isSuccessFullMessage(response)) {
                JsonObject user = response.getAsJsonObject("data");
                rc.request().headers().add("username", user.get("username").getAsString());
                rc.request().headers().add("role", user.get("role").getAsString());
                rc.next();
            } else {
                response = BaseResponse.createFullMessageResponse(
                        2, "unauthorized"
                );
                rc.response().end(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject response = BaseResponse.createFullMessageResponse(
                    1, "system_error"
            );
            rc.response().end(response.toString());
        }
    }
    public static void authorizeAdmin(RoutingContext rc) {
        try {
            int userid = Integer.parseInt(rc.request().getHeader("userid"));
            String token = rc.request().getHeader("token");
            JsonObject response = TalkHubServices.talkHubAuthService.authorize(userid, token);
            if (BaseResponse.isSuccessFullMessage(response)) {
                JsonObject user = response.getAsJsonObject("data");
                int role = user.get("role").getAsInt();
                if (role == 1) {
                    rc.request().headers().add("username", user.get("username").getAsString());
                    rc.request().headers().add("role", user.get("role").getAsString());
                    rc.next();
                } else {
                    response = BaseResponse.createFullMessageResponse(
                            2, "unauthorized"
                    );
                    rc.response().end(response.toString());
                }
            } else {
                response = BaseResponse.createFullMessageResponse(
                        2, "unauthorized"
                );
                rc.response().end(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject response = BaseResponse.createFullMessageResponse(
                    1, "system_error"
            );
            rc.response().end(response.toString());
        }
    }

    public static void authorizeSuperAdmin(RoutingContext rc) {
        try {
            int userid = Integer.parseInt(rc.request().getHeader("userid"));
            String token = rc.request().getHeader("token");
            JsonObject response = TalkHubServices.talkHubAuthService.authorize(userid, token);
            if (BaseResponse.isSuccessFullMessage(response)) {
                JsonObject user = response.getAsJsonObject("data");
                int role = user.get("role").getAsInt();
                if (role == 2) {
                    rc.request().headers().add("username", user.get("username").getAsString());
                    rc.request().headers().add("role", user.get("role").getAsString());
                    rc.next();
                } else {
                    response = BaseResponse.createFullMessageResponse(
                            2, "unauthorized"
                    );
                    rc.response().end(response.toString());
                }
            } else {
                response = BaseResponse.createFullMessageResponse(
                        2, "unauthorized"
                );
                rc.response().end(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject response = BaseResponse.createFullMessageResponse(
                    1, "system_error"
            );
        }
    }
    public static void register(RoutingContext rc) {
        try {
            String data = rc.body().asString();
            JsonObject json = GsonUtil.toJsonObject(data);
            String deviceId = rc.request().getParam("device_id","");
            String sessionInfo = rc.request().getParam("session_info","");
            json.addProperty("device_id",deviceId);
            json.addProperty("session_info", sessionInfo);
            JsonObject response = TalkHubServices.talkHubAuthService.register(json);
            rc.response().end(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject response = BaseResponse.createFullMessageResponse(
                    1, "system_error"
            );
            rc.response().end(response.toString());
        }
    }

    public static void login(RoutingContext rc) {
        try {
            /// Lấy request
            String data = rc.body().asString();
            JsonObject json = GsonUtil.toJsonObject(data);
            String deviceId = rc.request().getParam("device_id","");
            String sessionInfo = rc.request().getParam("session_info","");
            json.addProperty("device_id",deviceId);
            json.addProperty("session_info", sessionInfo);
            JsonObject response = TalkHubServices.talkHubAuthService.login(json);
            rc.response().end(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject response = BaseResponse.createFullMessageResponse(
                    1, "system_error"
            );
        }
    }
    public static void logout(RoutingContext rc) {
        try {
            String token = rc.request().getHeader("token");
            JsonObject response = TalkHubServices.talkHubAuthService.logout(token);
            rc.response().end(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject response = BaseResponse.createFullMessageResponse(
                    0, "success"
            );
            rc.response().end(response.toString());
        }
    }
    public static void refreshToken(RoutingContext rc) {
        try {
            long userid = Long.parseLong(rc.request().getHeader("userid"));
            JsonObject body = GsonUtil.toJsonObject(rc.body().asString());
            String refreshToken = body.get("refresh_token").getAsString();
            String deviceId;
            try{
                deviceId = body.get("device_id").getAsString();
            }catch (Exception e){
                deviceId = "";
            }
            if(deviceId.equals("")){
                deviceId = "" + userid;
            }
            JsonObject response = TalkHubServices.talkHubAuthService.refreshToken(userid, refreshToken, deviceId);
            rc.response().end(response.toString());
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            JsonObject response = BaseResponse.createFullMessageResponse(
                    1, "system_error"
            );
            rc.response().end(response.toString());
        }
    }
}
