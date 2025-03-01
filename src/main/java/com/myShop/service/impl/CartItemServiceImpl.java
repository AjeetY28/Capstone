package com.myShop.service.impl;

import com.myShop.entity.CartItem;
import com.myShop.entity.User;
import com.myShop.repository.CartItemRepository;
import com.myShop.service.CartItemService;
import com.myShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {
        CartItem item=findCartItemById(id);

        User cartItemUser=item.getCart().getUser();

        if(cartItemUser.getId().equals(userId))
        {
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity()*item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity()*item.getProduct().getSellingPrice());
            return cartItemRepository.save(item);
        }
        throw new Exception("You can't update this cart item");
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {
        CartItem item=findCartItemById(cartItemId);

        User cartItemUser=item.getCart().getUser();

        if(cartItemUser.getId().equals(userId)){
            cartItemRepository.delete(item);
        }
        else {
            throw new Exception("You can't remove this item");
        }

    }

    @Override
    public CartItem findCartItemById(Long id) throws Exception {

        return cartItemRepository.findById(id).orElseThrow(()->new Exception("Cart item not found"));
    }
}
