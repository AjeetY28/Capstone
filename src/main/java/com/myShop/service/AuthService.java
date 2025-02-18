package com.myShop.service;

import com.myShop.response.SignupRequest;

public interface AuthService {

    void sentLoginOtp(String email) throws Exception;
    String createUser(SignupRequest req) throws Exception;
}
