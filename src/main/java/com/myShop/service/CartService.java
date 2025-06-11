package com.myShop.service;

import com.myShop.entity.Cart;
import com.myShop.entity.CartItem;
import com.myShop.entity.Product;
import com.myShop.entity.User;
import com.myShop.exceptions.ProductException;

public interface CartService {

    CartItem addCartItem(
            User user,
            Product product,
            String size,
            int quantity) throws ProductException;

    Cart findUserCart(User user);
}
