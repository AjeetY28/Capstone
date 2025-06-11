package com.myShop.controller;


import com.myShop.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public ResponseEntity<ApiResponse> homeControllerHandler() {
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage("Ecommerce Application Developed By Ajeet Yadav");
        return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);

    }
}
