package com.talkhub.services.talkhub.auth;

import com.google.gson.JsonObject;

public interface ITalkHubAuthService {
    JsonObject register(JsonObject json);
    JsonObject login(JsonObject json);
    JsonObject generateLoginSession(JsonObject data, String deviceId, String sessionInfo);
    JsonObject authorize(long userId, String token);
    JsonObject logout(String accessToken);
    JsonObject refreshToken(long userId, String refreshToken, String deviceId);
}
