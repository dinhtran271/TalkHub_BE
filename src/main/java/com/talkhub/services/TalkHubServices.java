package com.talkhub.services;

import com.talkhub.services.talkhub.auth.AuthService;
import com.talkhub.services.talkhub.auth.IAuthService;

public class TalkHubServices {
  public static IAuthService authService = new AuthService();
}
