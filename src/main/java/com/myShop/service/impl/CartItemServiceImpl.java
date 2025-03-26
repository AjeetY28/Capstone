package com.myShop.service.impl;

import com.myShop.entity.CartItem;
import com.myShop.entity.User;
import com.myShop.exceptions.CartItemException;
import com.myShop.exceptions.UserException;
import com.myShop.repository.CartItemRepository;
import com.myShop.service.CartItemService;
import com.myShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem updateCartItem(Long userId,
                                   Long id,
                                   CartItem cartItem)
            throws CartItemException, UserException {

        CartItem item=findCartItemById(id);

        User cartItemUser=item.getCart().getUser();

        if(cartItemUser.getId().equals(userId))
        {
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity()*item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity()*item.getProduct().getSellingPrice());
            return cartItemRepository.save(item);
        }
        throw new CartItemException("You can't update this cart item");
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId)
            throws  CartItemException, UserException {

        CartItem item=findCartItemById(cartItemId);

        User cartItemUser=item.getCart().getUser();

        if(cartItemUser.getId().equals(userId)){
            cartItemRepository.deleteById(item.getId());
        }
        else {
            throw new UserException("You can't remove this item");
        }

    }

    @Override
    public CartItem findCartItemById(Long id) throws CartItemException {
        Optional<CartItem> opt=cartItemRepository.findById(id);

        if(opt.isPresent()) {
            return opt.get();
        }
        throw new CartItemException("cartItem not found with id : "+id);
    }
}
