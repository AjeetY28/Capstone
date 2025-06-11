package com.myShop.repository;

import com.myShop.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList,Long> {

    WishList findByUserId(Long userId);
}
