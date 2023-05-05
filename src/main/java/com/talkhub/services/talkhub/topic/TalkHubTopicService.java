package com.talkhub.services.talkhub.topic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.services.TalkHubServices;
import com.talkhub.util.json.GsonUtil;
import com.talkhub.util.log.DebugLogger;
import com.talkhub.util.sql.HikariClients;
import com.talkhub.util.sql.SQLJavaBridge;


public class TalkHubTopicService implements ITalkHubTopicService{

    @Override
    public JsonObject create(JsonObject data) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            JsonArray tags = data.get("tags").getAsJsonArray();
            data.addProperty("create_time", System.currentTimeMillis());
            data.addProperty("likes", 0);
            data.addProperty("view", 0);
            data.remove("tags");
            bridge.insertObjectToDB("th_topic", data);
            long topicId = data.get("id").getAsLong();
            for (int i=0; i<tags.size(); i++) {
                String nameTag = tags.get(i).getAsString();
                TalkHubServices.talkHubTagService.create(topicId, nameTag);
            }
            data.add("tags", tags);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject delete(long topicId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "DELETE FROM th_topic WHERE id=?";
            bridge.update(query, topicId);
            return BaseResponse.createFullMessageResponse(0, "success");
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getAllByUser(long userId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM th_topic WHERE userid=?";
            JsonArray topics = bridge.query(query, userId);
            JsonObject data = new JsonObject();
            JsonObject profile = TalkHubServices.talkHubProfileService.getInfo(userId).get("data").getAsJsonObject();
            String authorName = profile.get("nick_name").getAsString();
            String avatar = profile.get("avatar").getAsString();
            data.addProperty("author_name", authorName);
            data.addProperty("author_avatar", avatar);
            data.add("topics", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject update(long userId, JsonObject data) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            long topicId = data.get("id").getAsLong();
            String title = data.get("title").getAsString();
            JsonObject content = data.get("content").getAsJsonObject();
            JsonObject topic = new JsonObject();
            topic.addProperty("id", topicId);
            topic.addProperty("userid", userId);
            topic.addProperty("title", title);
            topic.add("content",  content);
            bridge.updateObjectToDb("th_topic", topic);
            return BaseResponse.createFullMessageResponse(0, "success", topic);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject updateLike(long topicId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "UPDATE th_topic set likes=likes+? WHERE id=?";
            bridge.update(query, 1, topicId);
            return BaseResponse.createFullMessageResponse(0, "success");
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getById(long topicId, long userId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM th_topic WHERE id=?";
            JsonObject data = bridge.queryOne(query, topicId);
            long authorId = data.get("userid").getAsLong();
            JsonObject authorProfile = TalkHubServices.talkHubProfileService.getInfo(authorId).get("data").getAsJsonObject();
            String topicAuthorName = authorProfile.get("nick_name").getAsString();
            String topicAuthorAvatar = authorProfile.get("avatar").getAsString();
            data.addProperty("topic_author_name", topicAuthorName);
            data.addProperty("topic_author_avatar", topicAuthorAvatar);
            if (authorId !=  userId) {
                updateView(topicId);
                long view = data.get("view").getAsLong();
                data.remove("view");
                data.addProperty("view", view + 1);
                boolean isMarked = TalkHubServices.talkHubBookmarkService.isMarked(userId, topicId);
                if (isMarked)
                    data.addProperty("marked", "true");
                else
                    data.addProperty("marked", "false");
            }
            long amoutPost = TalkHubServices.talkHubPostService.countPostOfTopic(topicId);
            data.addProperty("post", amoutPost);
            JsonObject tags = TalkHubServices.talkHubTagService.getAllByTopic(topicId).get("data").getAsJsonObject();
            JsonArray tagArray = tags.get("tags").getAsJsonArray();
            List<String> tagList = new ArrayList<>();
            for (int j=0; j<tagArray.size(); j++) {
                JsonObject tag = tagArray.get(j).getAsJsonObject();
                tagList.add(tag.get("name").getAsString());
            }
            data.add("tags", GsonUtil.toJsonArray(tagList.toString()));
            JsonArray posts = TalkHubServices.talkHubPostService.getAllByTopic(topicId).get("data").getAsJsonObject().get("posts").getAsJsonArray();
            data.add("posts", posts);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public long getUserId(long topciId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT userid FROM th_topic WHERE id=?";
            return bridge.queryLong(query, topciId);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return -1;
        }
    }

    @Override
    public JsonObject updateView(long topicId) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "UPDATE th_topic set view=view+? WHERE id=?";
            bridge.update(query, 1, topicId);
            return BaseResponse.createFullMessageResponse(0, "success");
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getLatest() {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM th_topic ORDER BY id DESC LIMIT ? OFFSET ?";
            JsonArray topics = bridge.query(query, 10, 0);
            for (int i=0; i<topics.size(); i++) {
                JsonObject topcic = topics.get(i).getAsJsonObject();
                topcic.remove("content");
                JsonArray imgs = topcic.get("img").getAsJsonArray();
                if (!imgs.isEmpty()){
                    topcic.remove("img");
                    topcic.addProperty("img", imgs.get(0).getAsString());
                } else
                    topcic.addProperty("img", "");
                long topicId = topcic.get("id").getAsLong();
                long amoutPost = TalkHubServices.talkHubPostService.countPostOfTopic(topicId);
                topcic.addProperty("post", amoutPost);
                long userId = topcic.get("userid").getAsLong();
                JsonObject profile = TalkHubServices.talkHubProfileService.getInfo(userId).get("data").getAsJsonObject();
                String authorName = profile.get("nick_name").getAsString();
                String avatar = profile.get("avatar").getAsString();
                topcic.addProperty("author_name", authorName);
                topcic.addProperty("author_avatar", avatar);
                JsonObject tags = TalkHubServices.talkHubTagService.getAllByTopic(topicId).get("data").getAsJsonObject();
                JsonArray tagArray = tags.get("tags").getAsJsonArray();
                List<String> tagList = new ArrayList<>();
                for (int j=0; j<tagArray.size(); j++) {
                    JsonObject tag = tagArray.get(j).getAsJsonObject();
                    tagList.add(tag.get("name").getAsString());
                }
                topcic.add("tags", GsonUtil.toJsonArray(tagList.toString()));
            }
            JsonObject data = new JsonObject();
            data.add("topics", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getPopularOnDay() {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM th_topic ORDER BY likes DESC LIMIT ? OFFSET ?";
            JsonArray topics = bridge.query(query, 10, 0);
            for (int i=0; i<topics.size(); i++) {
                JsonObject topcic = topics.get(i).getAsJsonObject();
                topcic.remove("content");
                JsonArray imgs = topcic.get("img").getAsJsonArray();
                if (!imgs.isEmpty()){
                    topcic.remove("img");
                    topcic.addProperty("img", imgs.get(0).getAsString());
                } else
                    topcic.addProperty("img", "");
                long topicId = topcic.get("id").getAsLong();
                long amoutPost = TalkHubServices.talkHubPostService.countPostOfTopic(topicId);
                topcic.addProperty("post", amoutPost);
                long userId = topcic.get("userid").getAsLong();
                JsonObject profile = TalkHubServices.talkHubProfileService.getInfo(userId).get("data").getAsJsonObject();
                String authorName = profile.get("nick_name").getAsString();
                String avatar = profile.get("avatar").getAsString();
                topcic.addProperty("author_name", authorName);
                topcic.addProperty("author_avatar", avatar);
                JsonObject tags = TalkHubServices.talkHubTagService.getAllByTopic(topicId).get("data").getAsJsonObject();
                JsonArray tagArray = tags.get("tags").getAsJsonArray();
                List<String> tagList = new ArrayList<>();
                for (int j=0; j<tagArray.size(); j++) {
                    JsonObject tag = tagArray.get(j).getAsJsonObject();
                    tagList.add(tag.get("name").getAsString());
                }
                topcic.add("tags", GsonUtil.toJsonArray(tagList.toString()));
            }
            JsonObject data = new JsonObject();
            data.add("topics", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getAll(int page, int count) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM th_topic ORDER BY view DESC LIMIT ? OFFSET ?";
            JsonArray topics = bridge.query(query, count, (page-1)*count);
            for (int i=0; i<topics.size(); i++) {
                JsonObject topcic = topics.get(i).getAsJsonObject();
                topcic.remove("content");
                JsonArray imgs = topcic.get("img").getAsJsonArray();
                if (!imgs.isEmpty()){
                    topcic.remove("img");
                    topcic.addProperty("img", imgs.get(0).getAsString());
                } else
                    topcic.addProperty("img", "");
                long topicId = topcic.get("id").getAsLong();
                long amoutPost = TalkHubServices.talkHubPostService.countPostOfTopic(topicId);
                topcic.addProperty("post", amoutPost);
                long userId = topcic.get("userid").getAsLong();
                JsonObject profile = TalkHubServices.talkHubProfileService.getInfo(userId).get("data").getAsJsonObject();
                String authorName = profile.get("nick_name").getAsString();
                String avatar = profile.get("avatar").getAsString();
                topcic.addProperty("author_name", authorName);
                topcic.addProperty("author_avatar", avatar);
                JsonObject tags = TalkHubServices.talkHubTagService.getAllByTopic(topicId).get("data").getAsJsonObject();
                JsonArray tagArray = tags.get("tags").getAsJsonArray();
                List<String> tagList = new ArrayList<>();
                for (int j=0; j<tagArray.size(); j++) {
                    JsonObject tag = tagArray.get(j).getAsJsonObject();
                    tagList.add(tag.get("name").getAsString());
                }
                topcic.add("tags", GsonUtil.toJsonArray(tagList.toString()));
            }
            JsonObject data = new JsonObject();
            data.add("topics", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getAllByCategory(long categoryId, int page, int count) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT * FROM th_topic WHERE categoryid=? LIMIT ? OFFSET ?";
            JsonArray topics = bridge.query(query, categoryId, count, (page-1)*count);
            for (int i=0; i<topics.size(); i++) {
                JsonObject topcic = topics.get(i).getAsJsonObject();
                topcic.remove("content");
                JsonArray imgs = topcic.get("img").getAsJsonArray();
                if (!imgs.isEmpty()){
                    topcic.remove("img");
                    topcic.addProperty("img", imgs.get(0).getAsString());
                } else
                    topcic.addProperty("img", "");
                long topicId = topcic.get("id").getAsLong();
                long amoutPost = TalkHubServices.talkHubPostService.countPostOfTopic(topicId);
                topcic.addProperty("post", amoutPost);
                long userId = topcic.get("userid").getAsLong();
                JsonObject profile = TalkHubServices.talkHubProfileService.getInfo(userId).get("data").getAsJsonObject();
                String authorName = profile.get("nick_name").getAsString();
                String avatar = profile.get("avatar").getAsString();
                topcic.addProperty("author_name", authorName);
                topcic.addProperty("author_avatar", avatar);
                JsonObject tags = TalkHubServices.talkHubTagService.getAllByTopic(topicId).get("data").getAsJsonObject();
                JsonArray tagArray = tags.get("tags").getAsJsonArray();
                List<String> tagList = new ArrayList<>();
                for (int j=0; j<tagArray.size(); j++) {
                    JsonObject tag = tagArray.get(j).getAsJsonObject();
                    tagList.add(tag.get("name").getAsString());
                }
                topcic.add("tags", GsonUtil.toJsonArray(tagList.toString()));
            }
            JsonObject data = new JsonObject();
            data.add("topics", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject getAllByTag(String tagName, int page, int count) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT th_topic.* FROM th_topic, th_tag WHERE " +
                    "th_tag.name=? AND th_topic.id = th_tag.topicid LIMIT ? OFFSET ?";
            JsonArray topics = bridge.query(query, tagName, count, (page-1)*count);
            for (int i=0; i<topics.size(); i++) {
                JsonObject topcic = topics.get(i).getAsJsonObject();
                topcic.remove("content");
                JsonArray imgs = topcic.get("img").getAsJsonArray();
                if (!imgs.isEmpty()){
                    topcic.remove("img");
                    topcic.addProperty("img", imgs.get(0).getAsString());
                } else
                    topcic.addProperty("img", "");
                long topicId = topcic.get("id").getAsLong();
                long amoutPost = TalkHubServices.talkHubPostService.countPostOfTopic(topicId);
                topcic.addProperty("post", amoutPost);
                long userId = topcic.get("userid").getAsLong();
                JsonObject profile = TalkHubServices.talkHubProfileService.getInfo(userId).get("data").getAsJsonObject();
                String authorName = profile.get("nick_name").getAsString();
                String avatar = profile.get("avatar").getAsString();
                topcic.addProperty("author_name", authorName);
                topcic.addProperty("author_avatar", avatar);
                JsonObject tags = TalkHubServices.talkHubTagService.getAllByTopic(topicId).get("data").getAsJsonObject();
                JsonArray tagArray = tags.get("tags").getAsJsonArray();
                List<String> tagList = new ArrayList<>();
                for (int j=0; j<tagArray.size(); j++) {
                    JsonObject tag = tagArray.get(j).getAsJsonObject();
                    tagList.add(tag.get("name").getAsString());
                }
                topcic.add("tags", GsonUtil.toJsonArray(tagList.toString()));
            }
            JsonObject data = new JsonObject();
            data.add("topics", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    @Override
    public JsonObject filter(String str, int page, int count) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String subString = "\"%"+ str + "%\"";
            String query = "SELECT * FROM th_topic WHERE title LIKE " + subString
                    + " ORDER BY likes, view DESC LIMIT ? OFFSET ?";
            JsonArray topics = bridge.query(query, count, (page-1)*count);
            for (int i=0; i<topics.size(); i++) {
                JsonObject topcic = topics.get(i).getAsJsonObject();
                topcic.remove("content");
                JsonArray imgs = topcic.get("img").getAsJsonArray();
                if (!imgs.isEmpty()){
                    topcic.remove("img");
                    topcic.addProperty("img", imgs.get(0).getAsString());
                } else
                    topcic.addProperty("img", "");
                long topicId = topcic.get("id").getAsLong();
                long amoutPost = TalkHubServices.talkHubPostService.countPostOfTopic(topicId);
                topcic.addProperty("post", amoutPost);
                long userId = topcic.get("userid").getAsLong();
                JsonObject profile = TalkHubServices.talkHubProfileService.getInfo(userId).get("data").getAsJsonObject();
                String authorName = profile.get("nick_name").getAsString();
                String avatar = profile.get("avatar").getAsString();
                topcic.addProperty("author_name", authorName);
                topcic.addProperty("author_avatar", avatar);
                JsonObject tags = TalkHubServices.talkHubTagService.getAllByTopic(topicId).get("data").getAsJsonObject();
                JsonArray tagArray = tags.get("tags").getAsJsonArray();
                List<String> tagList = new ArrayList<>();
                for (int j=0; j<tagArray.size(); j++) {
                    JsonObject tag = tagArray.get(j).getAsJsonObject();
                    tagList.add(tag.get("name").getAsString());
                }
                topcic.add("tags", GsonUtil.toJsonArray(tagList.toString()));
            }
            JsonObject data = new JsonObject();
            data.add("topics", topics);
            return BaseResponse.createFullMessageResponse(0, "success", data);
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }
}
