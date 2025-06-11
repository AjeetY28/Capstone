package com.myShop.repository;

import com.myShop.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon,Long> {

    Coupon findByCode(String code);
}
