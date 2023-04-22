package com.talkhub.vertx.talkhub;

import com.talkhub.vertx.talkhub.router.TalkHubAuthRouter;
import com.talkhub.vertx.talkhub.router.TalkHubBookMarkRouter;
import com.talkhub.vertx.talkhub.router.TalkHubCategoryRouter;
import com.talkhub.vertx.talkhub.router.TalkHubNotificationRouter;
import com.talkhub.vertx.talkhub.router.TalkHubPostRouter;
import com.talkhub.vertx.talkhub.router.TalkHubProfileRouter;
import com.talkhub.vertx.talkhub.router.TalkHubTagRouter;
import com.talkhub.vertx.talkhub.router.TalkHubTopicRouter;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class TalkHubAPI {
public static void configAPI(Router router) {
        authApi(router);
        profileAPI(router);
        categoryAPI(router);
        topicAPI(router);
        postAPI(router);
        tagAPI(router);
        bookmarkAPI(router);
        notificationAPI(router);
        }

        private static void authApi(Router router) {
        router.post("/ez/v1/register")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::register);
        router.post("/ez/v1/login")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::login);
        router.post("/ez/v1/logout")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::logout);
        router.post("/ez/v1/refreshtoken")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::refreshToken);
        }

        private static void profileAPI(Router router) {
        router.post("/ez/v1/profile/create")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubProfileRouter::create);
        router.post("/ez/v1/profile/update")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubProfileRouter::update);
        router.get("/ez/v1/profile/getinfo")
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubProfileRouter::getInfo);
        }

        private static void categoryAPI(Router router) {
        router.post("/ez/v1/category/create")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeAdmin)
                .handler(TalkHubCategoryRouter::create);
        router.post("/ez/v1/category/update")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeAdmin)
                .handler(TalkHubCategoryRouter::update);
        router.get("/ez/v1/category/getall")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubCategoryRouter::getAll);
        router.delete("/ez/v1/category/delete")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeAdmin)
                .handler(TalkHubCategoryRouter::delete);
        }

        private static void topicAPI(Router router) {
        router.post("/ez/v1/topic/create")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::create);
        router.post("/ez/v1/topic/update")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::update);
        router.delete("/ez/v1/topic/delete")
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::delete);
        router.get("/ez/v1/topic/getbyuser")
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::getAllByUser);
        router.get("/ez/v1/topic/getmytopic")
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::getMyTopic);
        router.post("/ez/v1/topic/updatelike")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::updateLike);
        router.get("/ez/v1/topic/getbyid")
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::getById);
        router.post("/ez/v1/topic/updateview")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::updateView);
        router.get("/ez/v1/topic/getbycategory")
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::getAllByCategory);
        router.get("/ez/v1/topic/getbytag")
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::getAllByTag);
        router.get("/ez/v1/topic/getall")
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::getAll);
        router.get("/ez/v1/topic/getlatest")
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::getLatest);
        router.get("/ez/v1/topic/getpopular")
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::getPopularOnDay);
        router.get("/ez/v1/topic/filter")
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTopicRouter::filter);
        }

        private static void postAPI(Router router) {
        router.post("/ez/v1/post/create")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubPostRouter::create);
        router.post("/ez/v1/post/update")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubPostRouter::update);
        router.post("/ez/v1/post/updatelike")
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubPostRouter::updateLike);
        router.delete("/ez/v1/post/delete")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubPostRouter::delete);
        router.get("/ez/v1/post/getbytopic")
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubPostRouter::getAllByTopic);
                
        }

        private static void tagAPI(Router router) {
        router.post("/ez/v1/tag/create")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTagRouter::create);
        router.delete("/ez/v1/tag/delete")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubTagRouter::delete);
        }

        
        private static void bookmarkAPI(Router router) {
        router.post("/ez/v1/bookmark/create")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubBookMarkRouter::create);
        router.delete("/ez/v1/bookmark/delete")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubBookMarkRouter::delete);
        router.get("/ez/v1/bookmark/gettopicmarked")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubBookMarkRouter::getAllTopicBookmark);        
        }

        private static void notificationAPI(Router router) {
        router.get("/ez/v1/noti/getall")
                .handler(BodyHandler.create())
                .handler(TalkHubAuthRouter::authorizeUser)
                .handler(TalkHubNotificationRouter::getAll);     
        }
}
