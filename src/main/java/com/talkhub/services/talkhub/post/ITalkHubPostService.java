package com.talkhub.services.talkhub.post;

import com.google.gson.JsonObject;

public interface ITalkHubPostService {
    JsonObject create(JsonObject data);
    JsonObject update(JsonObject data);
    JsonObject updateLike(long postId);
    JsonObject getAllByUser(long userId);
    JsonObject getAllByTopic(long topicId);
    JsonObject delete(long postId);
}
