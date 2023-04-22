package com.talkhub.services.talkhub.tag;

import com.google.gson.JsonObject;

public interface ITalkHubTagService {
    JsonObject create(JsonObject data);
    JsonObject getAllByTopic(long topicId);
    JsonObject delete(long tagId);
    JsonObject getAll();
}
