package com.talkhub.services.talkhub.notification;

import com.google.gson.JsonObject;

public interface ITalkHubNotificationService {
    JsonObject create(long userid, long otherid, long topciid, long createTime);
    JsonObject getAll(long userId);
}
