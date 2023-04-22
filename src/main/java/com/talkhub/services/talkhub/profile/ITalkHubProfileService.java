package com.talkhub.services.talkhub.profile;

import com.google.gson.JsonObject;

public interface ITalkHubProfileService {
    JsonObject create(long userId, JsonObject data);
    JsonObject create(long userId, String nickName);
    JsonObject update(JsonObject data);
    JsonObject getInfo(long userId);
}
