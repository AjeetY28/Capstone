package com.myShop.service;

import com.myShop.domain.AccountStatus;
import com.myShop.entity.Seller;

import java.util.List;

public interface SellerService {

    Seller getSellerProfile(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerById(Long id) throws Exception;
    Seller getSellerByEmail(String email) throws Exception;
    List<Seller> getAllSellers(AccountStatus status);
    Seller updateSeller(Seller seller,Long id);
    void deleteSeller(Long id);
    Seller verifyEmail(String email,String otp);
    Seller updateSellerStatus(Long sellerId, AccountStatus status);
}
