package com.myShop.service;

import com.myShop.entity.VerificationCode;

public interface VerificationService {
    VerificationCode createVerificationCode(String otp, String email);
}
