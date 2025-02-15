package com.myShop.service;

import com.myShop.response.SignupRequest;

public interface AuthService {

    String createUser(SignupRequest req);
}
