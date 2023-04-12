package com.talkhub.services.talkhub.follow;

import com.google.gson.JsonObject;

public interface ITalkHubFollowService {
    JsonObject create(JsonObject data);
    JsonObject delete(long userId, long otherId);
    JsonObject getAllFollowed(long userId);
    JsonObject getAllFollower(long otherId);
    JsonObject countFollowed(long userId);
    JsonObject countFollower(long otherId);
}
