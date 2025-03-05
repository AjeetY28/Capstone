package com.myShop.service;

import com.myShop.entity.Product;
import com.myShop.entity.User;
import com.myShop.entity.WishList;

public interface WishListService {

    WishList createWishList(User user);
    WishList getWishListByUserId(User user);
    WishList addProductToWishList(User user , Product product);
}
