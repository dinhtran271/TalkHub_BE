package com.talkhub.services.talkhub.topic;

import com.google.gson.JsonObject;

public interface ITalkHubTopicService {
    JsonObject create(JsonObject data);
    JsonObject update(JsonObject data);
    JsonObject updateLike(long topicId);
    JsonObject updateView(long topicId);
    JsonObject getAllByUser(long userId);
    JsonObject delete(long postId);
}
