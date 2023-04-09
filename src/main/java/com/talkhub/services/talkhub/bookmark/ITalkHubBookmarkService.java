package com.talkhub.services.talkhub.bookmark;

import com.google.gson.JsonObject;

public interface ITalkHubBookmarkService {
    JsonObject create(JsonObject data);
    JsonObject delete(long useriI, long topicId);
    JsonObject getAllByUser(long userId);
}
