package com.myShop.controller;

import com.myShop.domain.USER_ROLE;
import com.myShop.entity.User;
import com.myShop.entity.VerificationCode;
import com.myShop.repository.UserRepository;
import com.myShop.request.LoginOtpRequest;
import com.myShop.request.LoginRequest;
import com.myShop.response.ApiResponse;
import com.myShop.response.AuthResponse;
import com.myShop.response.SignupRequest;
import com.myShop.service.AuthService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception {


        String jwt=authService.createUser(req);

        AuthResponse res=new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("Register Success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(res);
    }


    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(
            @RequestBody LoginOtpRequest req) throws Exception {


        authService.sentLoginOtp(req.getEmail(),req.getRole());

        ApiResponse res=new ApiResponse();

        res.setMessage("Otp sent Successfully");

        return ResponseEntity.ok(res);
    }


    @PostMapping("/singing")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) throws Exception {


        AuthResponse authResponse =authService.signIn(req);


        return ResponseEntity.ok(authResponse);
    }
}
