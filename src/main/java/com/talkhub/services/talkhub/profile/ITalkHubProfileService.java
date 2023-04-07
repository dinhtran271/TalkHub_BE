package com.talkhub.services.talkhub.profile;

import com.google.gson.JsonObject;

public interface ITalkHubProfileService {
    JsonObject create(JsonObject data);
    JsonObject update(JsonObject data);
    JsonObject getInfo(long userId);
}
