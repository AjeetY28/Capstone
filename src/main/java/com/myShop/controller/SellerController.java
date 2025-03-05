package com.myShop.controller;


import com.myShop.domain.AccountStatus;
import com.myShop.entity.Seller;
import com.myShop.entity.SellerReport;
import com.myShop.entity.VerificationCode;
import com.myShop.exceptions.SellerException;
import com.myShop.repository.VerificationCodeRepository;
import com.myShop.request.LoginRequest;
import com.myShop.response.AuthResponse;
import com.myShop.service.AuthService;
import com.myShop.service.EmailService;
import com.myShop.service.SellerReportService;
import com.myShop.service.SellerService;
import com.myShop.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;
    private final SellerReportService sellerReportService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(
            @RequestBody LoginRequest req
            ) throws Exception {
        String otp=req.getOtp();
        String email=req.getEmail();

        req.setEmail("seller_"+email);

//        System.out.println(otp+" "+email);
        AuthResponse authResponse=authService.signIn(req);

        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(
            @PathVariable String otp) throws Exception {

        VerificationCode verificationCode=verificationCodeRepository.findByOtp(otp);

        if(verificationCode==null || !verificationCode.getOtp().equals(otp)){
            throw new Exception("wrong otp");
        }

        Seller seller=sellerService.verifyEmail(verificationCode.getEmail(),otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Seller>createSeller(
            @RequestBody Seller seller) throws Exception, MessagingException {

        Seller savedSeller=sellerService.createSeller(seller);

        String otp= OtpUtil.generateOtp();

        VerificationCode verificationCode=new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject="Ecommerce Email Verification Code";
        String text="Welcome to Ecommerce,verify your account using this link";
        String fronted_url="http://localhost:3000/verify-seller";
        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject,text+fronted_url);

        return new ResponseEntity<>(savedSeller,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller>getSellerById(@PathVariable Long id)throws SellerException {
        Seller seller=sellerService.getSellerById(id);
        return new ResponseEntity<>(seller,HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerProfile(
            @RequestHeader("Authorization") String jwt
    )throws Exception{

        Seller seller=sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller,HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(
            @RequestHeader("Authorization") String jwt
    )throws Exception{
        Seller seller=sellerService.getSellerProfile(jwt);
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
            @RequestBody Seller seller)throws Exception{

        Seller profile=sellerService.getSellerProfile(jwt);
        Seller updateSeller=sellerService.updateSeller(seller,profile.getId());

        return ResponseEntity.ok(updateSeller);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

}
