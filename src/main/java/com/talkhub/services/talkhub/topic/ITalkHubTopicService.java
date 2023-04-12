package com.talkhub.services.talkhub.topic;

import com.google.gson.JsonObject;

public interface ITalkHubTopicService {
    JsonObject create(JsonObject data);
    JsonObject update(long userId, JsonObject data);
    JsonObject updateLike(long topicId);
    JsonObject updateView(long topicId);
    JsonObject getAllByUser(long userId);
    JsonObject getById(long topicId);
    JsonObject delete(long topicId);
    JsonObject getAllByCategory(long categoryId, int page, int count);
    JsonObject getAllByTag(String tagId, int page, int count);
    JsonObject getAll(int page, int count);
}
