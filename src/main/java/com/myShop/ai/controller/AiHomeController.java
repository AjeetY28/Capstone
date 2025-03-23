package com.myShop.ai.controller;


import com.myShop.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AiHomeController {

    public ResponseEntity<ApiResponse> AiHome()
    {
        ApiResponse response=new ApiResponse();
        response.setMessage("Welcome to AI ChatBot");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
