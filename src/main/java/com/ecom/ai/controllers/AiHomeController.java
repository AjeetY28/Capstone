package com.ecom.ai.controllers;

import com.ecom.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AiHomeController {

    @GetMapping()
    public ResponseEntity<ApiResponse> AiHome(){
        ApiResponse response = new ApiResponse();
        response.setMessage("Welcome to Ecommerce AiBot");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
