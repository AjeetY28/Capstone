package com.myShop.service.impl;

import com.myShop.config.JwtProvider;
import com.myShop.domain.USER_ROLE;
import com.myShop.entity.Cart;
import com.myShop.entity.User;
import com.myShop.entity.VerificationCode;
import com.myShop.repository.CartRepository;
import com.myShop.repository.UserRepository;
import com.myShop.repository.VerificationCodeRepository;
import com.myShop.request.LoginRequest;
import com.myShop.response.AuthResponse;
import com.myShop.response.SignupRequest;
import com.myShop.service.AuthService;
import com.myShop.service.EmailService;
import com.myShop.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;
    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    public void sentLoginOtp(String email) throws Exception {
        String SIGNING_PREFIX="signing_";

        if(email.startsWith(SIGNING_PREFIX)){
            email=email.substring(SIGNING_PREFIX.length());

            User user=userRepository.findByEmail(email);
            if(user==null){
                throw new Exception("user not exist with provided email");
            }
        }
        VerificationCode isExist=verificationCodeRepository.findByEmail(email);
        if(isExist!=null)
        {
            verificationCodeRepository.delete(isExist);
        }

        String otp= OtpUtil.generateOtp();

        VerificationCode verificationCode=new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setOtp(otp);

        verificationCodeRepository.save(verificationCode);

        String subject="Ecommerce Login/Sing up Otp";

        String text="you Login/Sing up otp is - "+otp;

        emailService.sendVerificationOtpEmail(email,otp,subject,text);
    }

    @Override
    public String createUser(SignupRequest req) throws Exception {

        VerificationCode verificationCode=verificationCodeRepository.findByEmail(req.getEmail());

        if(verificationCode==null || !verificationCode.getOtp().equals(req.getOtp())) {
            throw new Exception("wrong otp...");
        }


        User user=userRepository.findByEmail(req.getEmail());

        if(user==null)
        {
            User createdUser=new User();
            createdUser.setName(req.getName());
            createdUser.setEmail(req.getEmail());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setPhone("9876543210");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));

            user=userRepository.save(createdUser);

            Cart cart=new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        List<GrantedAuthority>authorities=new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication=new UsernamePasswordAuthenticationToken(user.getEmail(),null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signIn(LoginRequest req) {
        String userName=req.getEmail();
        String otp=req.getOtp();


        Authentication authentication=authenticate(userName,otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token=jwtProvider.generateToken(authentication);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Success");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName=authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));
        return authResponse;
    }

    private Authentication authenticate(String userName, String otp) {
        UserDetails userDetails= customUserService.loadUserByUsername(userName);
        if(userDetails==null)
        {
            throw new BadCredentialsException("Invalid username or password");
        }

        VerificationCode verificationCode=verificationCodeRepository.findByEmail(userName);

        if(verificationCode==null || !verificationCode.getOtp().equals(otp))
        {
            throw new BadCredentialsException("wrong otp");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
    }
}
