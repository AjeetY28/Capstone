package com.myShop.service.impl;

import com.myShop.domain.USER_ROLE;
import com.myShop.entity.Seller;
import com.myShop.entity.User;
import com.myShop.repository.SellerRepository;
import com.myShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private static final String SELLER_PREFIX="seller_";

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        if(username.startsWith(SELLER_PREFIX)){
                String actualUserName =username.substring(SELLER_PREFIX.length());
                Seller seller =sellerRepository.findByEmail(actualUserName);

                if(seller!=null){
                    return buildUserDetails(seller.getEmail(),seller.getPassword(),seller.getRole());
                }
        }else {
            User user=userRepository.findByEmail(username); //custom user
            if(user!=null){
                return buildUserDetails(user.getEmail(),user.getPassword(),user.getRole());
            }
        }
        throw new UsernameNotFoundException("user or seller not found with email"+username);
    }

    private UserDetails buildUserDetails(String email, String password, USER_ROLE role) {
        if(role==null)
        {
            role=USER_ROLE.ROLE_CUSTOMER;
        }

        List<GrantedAuthority> authorityList=new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.toString()));
        return new org.springframework.security.core.userdetails.User(email,password,authorityList);
    }
}
