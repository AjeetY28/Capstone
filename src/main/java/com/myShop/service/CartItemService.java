package com.myShop.service;

import com.myShop.entity.CartItem;
import com.myShop.exceptions.CartItemException;
import com.myShop.exceptions.UserException;

public interface CartItemService {

    CartItem updateCartItem(Long userId,Long id, CartItem cartItem) throws  CartItemException, UserException;
    void removeCartItem(Long userId,Long cartItemId) throws CartItemException, UserException;
    CartItem findCartItemById(Long id) throws CartItemException;
}
