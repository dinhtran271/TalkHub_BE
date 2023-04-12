package com.talkhub.vertx.talkhub;

import com.talkhub.vertx.talkhub.router.AuthRouter;
import com.talkhub.vertx.talkhub.router.TalkHubCategoryRouter;
import com.talkhub.vertx.talkhub.router.TalkHubProfileRouter;
import com.talkhub.vertx.talkhub.router.TalkHubTopicRouter;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class TalkHubAPI {
  public static void configAPI(Router router) {
    authApi(router);
    profileAPI(router);
    categoryAPI(router);
    topicAPI(router);
}

private static void authApi(Router router) {
    router.post("/ez/v1/register")
            .handler(BodyHandler.create())
            .handler(AuthRouter::register);
    router.post("/ez/v1/login")
            .handler(BodyHandler.create())
            .handler(AuthRouter::login);
    router.post("/ez/v1/logout")
            .handler(BodyHandler.create())
            .handler(AuthRouter::authorizeUser)
            .handler(AuthRouter::logout);
    // router.post("/ez/v1/refreshtoken")
    //         .handler(BodyHandler.create())
    //         .handler(AuthRouter::refreshToken);
}

private static void profileAPI(Router router) {
    router.post("/ez/v1/profile/create")
            .handler(BodyHandler.create())
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubProfileRouter::create);
    router.post("/ez/v1/profile/update")
            .handler(BodyHandler.create())
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubProfileRouter::update);
    router.get("/ez/v1/profile/getinfo")
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubProfileRouter::getInfo);
}

private static void categoryAPI(Router router) {
    router.post("/ez/v1/category/create")
            .handler(BodyHandler.create())
            .handler(AuthRouter::authorizeAdmin)
            .handler(TalkHubCategoryRouter::create);
    router.post("/ez/v1/category/update")
            .handler(BodyHandler.create())
            .handler(AuthRouter::authorizeAdmin)
            .handler(TalkHubCategoryRouter::update);
    router.get("/ez/v1/category/getall")
            .handler(BodyHandler.create())
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubCategoryRouter::getAll);
    router.delete("/ez/v1/category/delete")
            .handler(BodyHandler.create())
            .handler(AuthRouter::authorizeAdmin)
            .handler(TalkHubCategoryRouter::delete);
}

private static void topicAPI(Router router) {
    router.post("/ez/v1/topic/create")
            .handler(BodyHandler.create())
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubTopicRouter::create);
    router.post("/ez/v1/topic/update")
            .handler(BodyHandler.create())
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubTopicRouter::update);
    router.delete("/ez/v1/topic/delete")
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubTopicRouter::delete);
    router.get("/ez/v1/topic/getbyuser")
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubTopicRouter::getAllByUser);
    router.get("/ez/v1/topic/getmytopic")
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubTopicRouter::getMyTopic);
    router.post("/ez/v1/topic/updatelike")
            .handler(BodyHandler.create())
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubTopicRouter::updateLike);
    router.get("/ez/v1/topic/getbyid")
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubTopicRouter::getById);
    router.post("/ez/v1/topic/updateview")
            .handler(BodyHandler.create())
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubTopicRouter::updateView);
    router.get("/ez/v1/topic/getbycategory")
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubTopicRouter::getAllByCategory);
    router.get("/ez/v1/topic/getbytag")
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubTopicRouter::getAllByTag);
    router.get("/ez/v1/topic/getall")
            .handler(AuthRouter::authorizeUser)
            .handler(TalkHubTopicRouter::getAll);
}
}
