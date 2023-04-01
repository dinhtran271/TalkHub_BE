package com.talkhub.services.talkhub.auth;

import com.google.gson.JsonObject;

public interface IAuthService {
  JsonObject register(JsonObject json);
  JsonObject login(JsonObject json);
  JsonObject authorize(long userId, String token);
  JsonObject logout(long userId);
}
