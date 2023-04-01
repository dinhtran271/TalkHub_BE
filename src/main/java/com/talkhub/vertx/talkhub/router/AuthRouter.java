package com.talkhub.vertx.talkhub.router;

import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.services.TalkHubServices;
import com.talkhub.util.json.GsonUtil;
import io.vertx.ext.web.RoutingContext;

public class AuthRouter {
  public static void authorizeUser(RoutingContext rc) {
    try {
      long userId = Long.parseLong(rc.request().getHeader("userid"));
      String token = rc.request().getHeader("token");
      JsonObject response = TalkHubServices.authService.authorize(userId, token);
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
      JsonObject response = TalkHubServices.authService.authorize(userid, token);
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
    }
  }

  public static void authorizeSuperAdmin(RoutingContext rc) {
    try {
      int userid = Integer.parseInt(rc.request().getHeader("userid"));
      String token = rc.request().getHeader("token");
      JsonObject response = TalkHubServices.authService.authorize(userid, token);
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
      JsonObject response = TalkHubServices.authService.register(json);
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
      JsonObject response = TalkHubServices.authService.login(json);
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
      int user_id = Integer.parseInt(rc.request().getHeader("user_id"));
      JsonObject response = TalkHubServices.authService.logout(user_id);
      rc.response().end(response.toString());
    } catch (Exception e) {
      e.printStackTrace();
      JsonObject response = BaseResponse.createFullMessageResponse(
        0, "system_error"
      );
      rc.response().end(response.toString());
    }
  }
}
