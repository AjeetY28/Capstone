package com.myShop.controller;


import com.myShop.config.JwtProvider;
import com.myShop.domain.AccountStatus;
import com.myShop.domain.USER_ROLE;
import com.myShop.entity.Seller;
import com.myShop.entity.SellerReport;
import com.myShop.entity.VerificationCode;
import com.myShop.exceptions.SellerException;
import com.myShop.repository.VerificationCodeRepository;
import com.myShop.request.LoginRequest;
import com.myShop.response.ApiResponse;
import com.myShop.response.AuthResponse;
import com.myShop.service.*;
import com.myShop.service.impl.CustomUserServiceImpl;
import com.myShop.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationService verificationService;
    private final AuthService authService;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;
    private final SellerReportService sellerReportService;
    private final CustomUserServiceImpl customUserServiceImpl;

    @PostMapping("/sent/login-otp")
    public ResponseEntity<ApiResponse> sentLoginOtp(
            @RequestBody VerificationCode req)throws MessagingException,SellerException{


        Seller seller=sellerService.getSellerByEmail(req.getEmail());

        String otp=OtpUtil.generateOtp();
        VerificationCode verificationCode=verificationService.createVerificationCode(otp,req.getEmail());

        String subject="Ecommerce Login Otp";
        String text="Your login otp is - ";
        emailService.sendVerificationOtpEmail(req.getEmail(),verificationCode.getOtp(),subject,text);

        ApiResponse res=new ApiResponse();
        res.setMessage("Otp Sent");

        return new ResponseEntity<>(res,HttpStatus.CREATED);
    }


//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> loginSeller(
//            @RequestBody LoginRequest req
//            ) throws Exception {
//        String otp=req.getOtp();
//        String email=req.getEmail();
//
//        req.setEmail("seller_"+email);
//
////        System.out.println(otp+" "+email);
//        AuthResponse authResponse=authService.signIn(req);
//
//        return ResponseEntity.ok(authResponse);
//    }

    @PostMapping("/verify/login-otp")
    public ResponseEntity<AuthResponse> verifyLoginOtp(
            @RequestBody VerificationCode req)throws MessagingException,SellerException {

        String otp=req.getOtp();
        String email= req.getEmail();
        VerificationCode verificationCode=verificationCodeRepository.findByEmail(email);

        if(verificationCode==null || !verificationCode.getOtp().equals(otp))
        {
            throw new SellerException("Wrong otp....");
        }

        Authentication authentication=authenticate(req.getEmail());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token=jwtProvider.generateToken(authentication);
        AuthResponse authResponse=new AuthResponse();

        authResponse.setMessage("Login Success");
        authResponse.setJwt(token);

        Collection<? extends GrantedAuthority>authorities=authentication.getAuthorities();

        String roleName=authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.OK);
    }

    private Authentication authenticate(String username){
        UserDetails userDetails=customUserServiceImpl.loadUserByUsername("seller_"+username);

        if(userDetails==null)
        {
            throw new BadCredentialsException("Invalid Username or Password");

        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(
            @PathVariable String otp) throws SellerException {

        VerificationCode verificationCode=verificationCodeRepository.findByOtp(otp);

        if(verificationCode==null || !verificationCode.getOtp().equals(otp)){
            throw new SellerException("wrong otp.....");
        }

        Seller seller=sellerService.verifyEmail(verificationCode.getEmail(),otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Seller>createSeller(
            @RequestBody Seller seller) throws SellerException, MessagingException {

        Seller savedSeller=sellerService.createSeller(seller);

        String otp= OtpUtil.generateOtp();
        VerificationCode verificationCode=verificationService.createVerificationCode(otp,seller.getEmail());

//        VerificationCode verificationCode=new VerificationCode();
//        verificationCode.setOtp(otp);
//        verificationCode.setEmail(seller.getEmail());
//        verificationCodeRepository.save(verificationCode);

        String subject="Ecommerce Email Verification Code";
        String text="Welcome to Ecommerce,verify your account using this link";
        String fronted_url="http://localhost:3000/verify-seller";
        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject,text+fronted_url);

        return new ResponseEntity<>(savedSeller,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller>getSellerById(
            @PathVariable Long id)throws SellerException {

        Seller seller=sellerService.getSellerById(id);
        return new ResponseEntity<>(seller,HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerProfile(
            @RequestHeader("Authorization") String jwt
    )throws SellerException{

        String email=jwtProvider.getEmailFromJwtToken(jwt);
        Seller seller=sellerService.getSellerByEmail(email);
        return new ResponseEntity<>(seller,HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(
            @RequestHeader("Authorization") String jwt
    )throws SellerException{

        String email=jwtProvider.getEmailFromJwtToken(jwt);
        Seller seller=sellerService.getSellerByEmail(email);
        SellerReport report=sellerReportService.getSellerReport(seller);
        return new ResponseEntity<>(report,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSeller(
            @RequestParam(required = false)AccountStatus status){

        List<Seller> sellers=sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers);
    }

    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(
            @RequestHeader("Authorization") String jwt,
            @RequestBody Seller seller)throws SellerException{

        Seller profile=sellerService.getSellerProfile(jwt);
        Seller updateSeller=sellerService.updateSeller(seller,profile.getId());

        return ResponseEntity.ok(updateSeller);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws SellerException {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

}
