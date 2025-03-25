package com.myShop.service;

import com.myShop.domain.AccountStatus;
import com.myShop.entity.Seller;
import com.myShop.exceptions.SellerException;

import java.util.List;

public interface SellerService {

    Seller getSellerProfile(String jwt) throws SellerException;
    Seller createSeller(Seller seller) throws SellerException;
    Seller getSellerById(Long id) throws SellerException;
    Seller getSellerByEmail(String email) throws SellerException;
    List<Seller> getAllSellers(AccountStatus status);
    Seller updateSeller(Seller seller,Long id) throws SellerException;
    void deleteSeller(Long id) throws SellerException;
    Seller verifyEmail(String email,String otp) throws SellerException;
    Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws SellerException;
}
