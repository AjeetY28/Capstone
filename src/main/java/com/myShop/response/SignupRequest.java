package com.myShop.response;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SignupRequest {

    private String email;
    private String name;
    private String otp;
//    private String password;
}
