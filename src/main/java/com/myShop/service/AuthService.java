package com.myShop.service;

import com.myShop.domain.USER_ROLE;
import com.myShop.request.LoginRequest;
import com.myShop.response.AuthResponse;
import com.myShop.response.SignupRequest;

public interface AuthService {

    void sentLoginOtp(String email, USER_ROLE role) throws Exception;
    String createUser(SignupRequest req) throws Exception;

    AuthResponse signIn(LoginRequest req);
}
