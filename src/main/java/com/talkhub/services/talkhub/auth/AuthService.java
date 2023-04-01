package com.talkhub.services.talkhub.auth;

import com.google.gson.JsonObject;
import com.talkhub.common.BaseResponse;
import com.talkhub.util.sql.HikariClients;
import com.talkhub.util.sql.SQLJavaBridge;
import com.talkhub.util.string.StringUtil;

public class AuthService implements IAuthService{
  public JsonObject register(JsonObject json){
    try{
      String username = json.get("username").getAsString();
      String password = json.get("password").getAsString();
      return register(username, password);
    }catch (Exception e){
      e.printStackTrace();
      return BaseResponse.createFullMessageResponse(1, "system_error");
    }
  }
  private JsonObject register(String username, String password){
    try{
      SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
      String query = "SELECT id FROM user WHERE username = ?";

      boolean isExisted = bridge.queryExist(query, username);

      if(isExisted){
        return BaseResponse.createFullMessageResponse(10, "user_exist");
      }

      String token = StringUtil.generateRandomStringNumberCharacter(32);
      query = "INSERT INTO user (username, password, token, role, status, createTime) VALUE (?,?,?,?,?,?)";

      String hashedPassword = StringUtil.sha256(password);

      long createTime = System.currentTimeMillis();
      bridge.update(query, username, hashedPassword, token, 0, 0, createTime);
      return login(username, password);
    }catch (Exception e){
      return BaseResponse.createFullMessageResponse(1, "system_error");
    }
  }
  public JsonObject login(JsonObject json){
    try{
      String username = json.get("username").getAsString();
      String password = json.get("password").getAsString();
      return login(username, password);
    }catch (Exception e){
      e.printStackTrace();
      return BaseResponse.createFullMessageResponse(1, "system_error");
    }
  }
  private JsonObject login(String username, String password){
    try{
      SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();

      String query = "SELECT * FROM user WHERE username = ?";

      JsonObject data = bridge.queryOne(query, username);

      if(data == null){
        return BaseResponse.createFullMessageResponse(10, "invalid_username");
      }
      String storedPassword = data.get("password").getAsString();
      String hashedPassword = StringUtil.sha256(password);
      if(!storedPassword.equals(hashedPassword)){
        return BaseResponse.createFullMessageResponse(11, "invalid_password");
      }

      data.remove("password");
      return BaseResponse.createFullMessageResponse(0, "success", data);
    }catch (Exception e){
      e.printStackTrace();
      return BaseResponse.createFullMessageResponse(1, "system_error");
    }
  }
  public JsonObject logout(long userId)  {
    try{
      SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
      String query = "UPDATE user SET token=? WHERE id=?";
      String token = StringUtil.generateRandomStringNumberCharacter(32);
      bridge.update(query, token, userId);
      return BaseResponse.createFullMessageResponse(0, "success");
    }catch (Exception e){
      return BaseResponse.createFullMessageResponse(1, "system_error");
    }
  }
  public JsonObject authorize(long userId, String token) {
    try{
      SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
      String query = "SELECT * FROM user WHERE token=?";
      JsonObject data = bridge.queryOne(query, token);
      if(data == null){
        return BaseResponse.createFullMessageResponse(10, "invalid_token");
      }
      int status = data.get("status").getAsInt();
      if(status != 0){
        return BaseResponse.createFullMessageResponse(11, "account_locked", data);
      }
      data.remove("password");
      return BaseResponse.createFullMessageResponse(0, "success", data);
    }catch (Exception e){
      e.printStackTrace();
      return BaseResponse.createFullMessageResponse(1, "system_error");
    }
  }
}
