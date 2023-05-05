package com.talkhub.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;

import com.google.gson.JsonObject;
import com.talkhub.util.file.FileUtil;
import com.talkhub.vertx.talkhub.TalkHubAPI;

import java.util.HashSet;
import java.util.Set;

public class HttpServer extends AbstractVerticle {
  private io.vertx.core.http.HttpServer httpServer;
  @Override
  public void start(){
    JsonObject config = FileUtil.getJsonObject("config/http/httpserver.json", false);
    Router router = Router.router(vertx);
    crossAccessControl(router);
    router.route("/v1/test").handler(this::test);
    TalkHubAPI.config(router);
    httpServer = vertx.createHttpServer()
      .requestHandler(router)
      .listen(config.get("port").getAsInt()).result();
  }

  private void test(RoutingContext rc) {
    rc.response().end("OK");
  }

  @Override
  public void stop() {
    if (httpServer != null) httpServer.close();
  }

  public void crossAccessControl(Router router) {
    Set<String> allowedHeaders = new HashSet<>();
    allowedHeaders.add("*");
    Set<HttpMethod> allowedMethods = new HashSet<>();
    allowedMethods.add(HttpMethod.GET);
    allowedMethods.add(HttpMethod.POST);
    allowedMethods.add(HttpMethod.PUT);
    allowedMethods.add(HttpMethod.DELETE);
    allowedMethods.add(HttpMethod.OPTIONS);
    router.route().handler(CorsHandler.create()
      .allowedHeaders(allowedHeaders)
      .allowedMethods(allowedMethods));
  }
}
