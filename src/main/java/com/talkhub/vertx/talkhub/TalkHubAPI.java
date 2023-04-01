package com.talkhub.vertx.talkhub;

import com.talkhub.vertx.talkhub.router.AuthRouter;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class TalkHubAPI {
  public static void configAPI(Router router) {
    authApi(router);
  }

  private static void authApi(Router router) {
    router.post("/v1/register")
      .handler(BodyHandler.create())
      .handler(AuthRouter::register);
    router.post("/v1/login")
      .handler(BodyHandler.create())
      .handler(AuthRouter::login);
    router.post("/v1/logout")
      .handler(AuthRouter::authorizeUser)
      .handler(AuthRouter::logout);
  }
}
