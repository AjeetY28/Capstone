package com.myShop.controller;

import com.myShop.domain.USER_ROLE;
import com.myShop.entity.User;
import com.myShop.entity.VerificationCode;
import com.myShop.exceptions.SellerException;
import com.myShop.exceptions.UserException;
import com.myShop.repository.UserRepository;
import com.myShop.request.LoginOtpRequest;
import com.myShop.request.LoginRequest;
import com.myShop.response.ApiResponse;
import com.myShop.response.AuthResponse;
import com.myShop.response.SignupRequest;
import com.myShop.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(
            @Valid
            @RequestBody SignupRequest req) throws SellerException {


        String jwt=authService.createUser(req);

        AuthResponse res=new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("Register Success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(
            @RequestBody VerificationCode req) throws MessagingException, UserException {


        authService.sentLoginOtp(req.getEmail());

        ApiResponse res=new ApiResponse();

        res.setMessage("Otp sent Successfully");

        return new ResponseEntity<>(res,HttpStatus.CREATED);
    }


    @PostMapping("/singing")
    public ResponseEntity<AuthResponse> loginHandler(
            @RequestBody LoginRequest req) throws SellerException {


        AuthResponse authResponse =authService.signIn(req);


        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
