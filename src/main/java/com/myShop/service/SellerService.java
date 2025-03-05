package com.myShop.service;

import com.myShop.domain.AccountStatus;
import com.myShop.entity.Seller;
import com.myShop.exceptions.SellerException;

import java.util.List;

public interface SellerService {

    Seller getSellerProfile(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerById(Long id) throws SellerException;
    Seller getSellerByEmail(String email) throws Exception;
    List<Seller> getAllSellers(AccountStatus status);
    Seller updateSeller(Seller seller,Long id) throws Exception;
    void deleteSeller(Long id) throws Exception;
    Seller verifyEmail(String email,String otp) throws Exception;
    Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception;
}
