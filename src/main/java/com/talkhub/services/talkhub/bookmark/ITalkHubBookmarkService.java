package com.talkhub.services.talkhub.bookmark;

import com.google.gson.JsonObject;

public interface ITalkHubBookmarkService {
    JsonObject create(JsonObject data);
    JsonObject update(JsonObject data);
    JsonObject delete(long bookmarkId);
    JsonObject getAllByUser(long userId);
}
