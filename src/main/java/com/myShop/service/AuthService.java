package com.myShop.service;

import com.myShop.exceptions.SellerException;
import com.myShop.exceptions.UserException;
import com.myShop.request.LoginRequest;
import com.myShop.response.AuthResponse;
import com.myShop.response.SignupRequest;
import jakarta.mail.MessagingException;

public interface AuthService {

    void sentLoginOtp(String email) throws MessagingException, UserException;
    String createUser(SignupRequest req) throws SellerException;
    AuthResponse signIn(LoginRequest req) throws SellerException;
}
