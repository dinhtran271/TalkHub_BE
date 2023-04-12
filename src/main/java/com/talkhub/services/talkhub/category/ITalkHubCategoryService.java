package com.talkhub.services.talkhub.category;

import com.google.gson.JsonObject;

public interface ITalkHubCategoryService {
    JsonObject create(JsonObject data);
    JsonObject update(JsonObject data);
    JsonObject delete(long categoryId);
    JsonObject getAll();
}
