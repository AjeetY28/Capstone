package com.myShop.service;

import com.myShop.entity.Cart;
import com.myShop.entity.Coupon;
import com.myShop.entity.User;

import java.util.List;

public interface CouponService {

    Cart applyCoupon(String code, double orderValue, User user) throws Exception;
    Cart removeCoupon(String code,User user) throws Exception;
    Coupon createCoupon(Coupon coupon);
    void deleteCoupon(Long id) throws Exception;
    List<Coupon> findAllCoupons();

    Coupon getCouponById(Long id) throws Exception;
}
