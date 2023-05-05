package com.talkhub.services.talkhub.auth;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.services.TalkHubServices;
import com.talkhub.util.log.DebugLogger;
import com.talkhub.util.sql.HikariClients;
import com.talkhub.util.sql.SQLJavaBridge;
import com.talkhub.util.string.StringUtil;

public class TalkHubAuthService implements ITalkHubAuthService {
    final static long ACCESS_TOKEN_EXPIRATION_TIME = 3600000l * 24 * 7;
    final static long REFRESH_TOKEN_EXPIRATION_TIME = 3600000l * 24 * 30;

    public JsonObject register(JsonObject json){
        try{
            String username = json.get("username").getAsString();
            String password = json.get("password").getAsString();
            String deviceId = json.get("device_id").getAsString();
            String sessionInfo = json.get("session_info").getAsString();
            return register(username, password, deviceId, sessionInfo);
        }catch (Exception e){
            e.printStackTrace();
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }
    private JsonObject register(String username, String password, String deviceId, String sessionInfo){
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT id FROM th_user WHERE username = ?";

            boolean isExisted = bridge.queryExist(query, username);

            if(isExisted){
                return BaseResponse.createFullMessageResponse(12, "user_exist");
            }

            String refreshToken = StringUtil.generateRandomStringNumberCharacter(32);
            query = "INSERT INTO th_user (username, password, role, status, createTime, refresh_token, token_exp_time) VALUE (?,?,?,?,?,?,?)";

            String hashedPassword = StringUtil.sha256(password);

            long createTime = System.currentTimeMillis();
            bridge.update(query, username, hashedPassword, 0, 0, createTime, refreshToken, createTime+ REFRESH_TOKEN_EXPIRATION_TIME);
            return login(username, password, deviceId, sessionInfo);
        }catch (Exception e){
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }
    public JsonObject login(JsonObject json) {
        try{
            String username = json.get("username").getAsString();
            String password = json.get("password").getAsString();
            String deviceId = json.get("device_id").getAsString();
            String sessionInfo = json.get("session_info").getAsString();
            return login(username, password, deviceId, sessionInfo);
        }catch (Exception e){
            e.printStackTrace();
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }


    public JsonObject generateLoginSession(JsonObject data, String deviceId, String sessionInfo) {
        try {
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query;
            data.remove("password");

            long userId = data.get("id").getAsLong();
            if (deviceId.equals(""))
                deviceId = "" + userId;

            //check refresh token expired -> refresh
            int row;
            long current = System.currentTimeMillis();
            long refExpTime = data.get("token_exp_time").getAsLong();
            if (current >= refExpTime) {
                String newRefreshToken = StringUtil.generateRandomStringNumberCharacter(32);
                refExpTime = current + REFRESH_TOKEN_EXPIRATION_TIME;
                query = "UPDATE th_user SET refresh_token = ?, token_exp_time = ? WHERE id = ?";
                row = bridge.update(query, newRefreshToken, refExpTime, userId);
                if (row == 0) {
                    return BaseResponse.createFullMessageResponse(1, "system_error");
                }
            }

            query = "SELECT * FROM th_login_session WHERE userid = ? AND device_id = ?";
            JsonObject session = bridge.queryOne(query, userId, deviceId);
            if (session == null) {
                //neu chua co phien dang nhap nay
                query = "INSERT INTO th_login_session(userid, device_id, token, token_exp_time, session_info) VALUES (?,?,?,?,?)";
                String token = StringUtil.generateRandomStringNumberCharacter(32);
                long tokExpTime = current + ACCESS_TOKEN_EXPIRATION_TIME;
                row = bridge.update(query, userId, deviceId, token, tokExpTime, sessionInfo);
                if (row == 0) {
                    return BaseResponse.createFullMessageResponse(1, "system_error");
                }
                data.addProperty("token", token);
                data.addProperty("access_token_exp_time", tokExpTime);
                return BaseResponse.createFullMessageResponse(0, "success", data);
            } else {
                //neu da co  phien dang nhap nay
                //kiem tra thoi gian xem phien dang nhap co het han hay khong
                long tokExpTime = session.get("token_exp_time").getAsLong();
                if (current >= tokExpTime) {   //neu phien het han
                    query = "UPDATE th_login_session SET token = ?, token_exp_time = ?, session_info = ? WHERE userid= ? AND device_id = ?";
                    String token = StringUtil.generateRandomStringNumberCharacter(32);
                    tokExpTime = current + ACCESS_TOKEN_EXPIRATION_TIME;
                    row = bridge.update(query, token, tokExpTime, sessionInfo, userId, deviceId);
                    if (row == 0) {
                        return BaseResponse.createFullMessageResponse(1, "system_error");
                    }
                    data.addProperty("token", token);
                    data.addProperty("access_token_exp_time", tokExpTime);
                    return BaseResponse.createFullMessageResponse(0, "success", data);
                } else {
                    String token = session.get("token").getAsString();
                    long access_token_exp_time = session.get("token_exp_time").getAsLong();
                    query = "UPDATE th_login_session SET session_info = ? WHERE userid= ? AND device_id = ?";
                    row = bridge.update(query, sessionInfo, userId, deviceId);
                    if (row == 0) {
                        return BaseResponse.createFullMessageResponse(1, "system_error");
                    }
                    data.addProperty("token", token);
                    data.addProperty("access_token_exp_time", access_token_exp_time);
                    return BaseResponse.createFullMessageResponse(0, "success", data);
                }
            }
        } catch (Exception e) {
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    private JsonObject login(String username, String password, String deviceId, String sessionInfo){
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();

            String query = "SELECT * FROM th_user WHERE username = ?";

            JsonObject data = bridge.queryOne(query, username);

            if(data == null){
                return BaseResponse.createFullMessageResponse(10, "invalid_username");
            }
            String storedPassword = data.get("password").getAsString();
            String hashedPassword = StringUtil.sha256(password);
            if(!storedPassword.equals(hashedPassword)){
                return BaseResponse.createFullMessageResponse(11, "invalid_password");
            }
            long userId = data.get("id").getAsLong();
            JsonObject profile = TalkHubServices.talkHubProfileService.getInfo(userId).get("data").getAsJsonObject();
            JsonObject session = generateLoginSession(data, deviceId, sessionInfo);
            session.get("data").getAsJsonObject().addProperty("nick_name", profile.get("nick_name").getAsString());
            session.get("data").getAsJsonObject().addProperty("avatar", profile.get("avatar").getAsString());
            return session;
        }catch (Exception e){
            e.printStackTrace();
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    public JsonObject logout(String accessToken)  {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "UPDATE th_login_session SET token=? WHERE token=?";
            String token = StringUtil.generateRandomStringNumberCharacter(32);
            int row = bridge.update(query, token, accessToken);
            if (row == 0) {
                return BaseResponse.createFullMessageResponse(2, "invalid_token");
            }
            return BaseResponse.createFullMessageResponse(0, "success");
        }catch (Exception e){
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }
    public JsonObject authorize(long userId, String token) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT th_user.username, th_user.role, th_user.status, ls.device_id FROM  th_login_session ls, th_user WHERE  ls.token=? AND ls.userid = ? AND ls.userid = th_user.id";
            JsonObject data = bridge.queryOne(query, token, userId);
            if(data == null){
                return BaseResponse.createFullMessageResponse(10, "invalid_token");
            }
            int status = data.get("status").getAsInt();
            if(status != 0){
                return BaseResponse.createFullMessageResponse(11, "account_locked", data);
            }
            return BaseResponse.createFullMessageResponse(0, "success", data);
        }catch (Exception e){
            e.printStackTrace();
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }

    public JsonObject refreshToken(long userId, String refreshToken,String deviceId){
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = "SELECT token_exp_time FROM th_user WHERE refresh_token = ?";
            Long refExpTime = bridge.queryLong(query, refreshToken);
            if(refExpTime == null){
                return BaseResponse.createFullMessageResponse(10,"invalid_refresh_token");
            }else{
                long current = System.currentTimeMillis();
                if(current >= refExpTime){
                    return BaseResponse.createFullMessageResponse(11,"refresh_token_expired");
                }else{
                    query = "UPDATE th_login_session SET token = ?, token_exp_time = ? WHERE userid = ? AND device_id = ?";
                    String token = StringUtil.generateRandomStringNumberCharacter(32);
                    int row = bridge.update(query,token, current + ACCESS_TOKEN_EXPIRATION_TIME, userId, deviceId);
                    if(row == 0){
                        return BaseResponse.createFullMessageResponse(1,"system_error");
                    }
                    JsonObject response = new JsonObject();
                    response.addProperty("token",token);
                    response.addProperty("token_exp_time",current + ACCESS_TOKEN_EXPIRATION_TIME);
                    return BaseResponse.createFullMessageResponse(0, "success", response);
                }
            }
        }catch (Exception e){
            String stacktrace = ExceptionUtils.getStackTrace(e);
            DebugLogger.error(stacktrace);
            return BaseResponse.createFullMessageResponse(1, "system_error");
        }
    }
}
