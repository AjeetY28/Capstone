package com.myShop.repository;

import com.myShop.entity.Cart;
import com.myShop.entity.CartItem;
import com.myShop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
