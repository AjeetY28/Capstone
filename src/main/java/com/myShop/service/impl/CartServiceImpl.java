package com.myShop.service.impl;

import com.myShop.entity.Cart;
import com.myShop.entity.CartItem;
import com.myShop.entity.Product;
import com.myShop.entity.User;
import com.myShop.repository.CartItemRepository;
import com.myShop.repository.CartRepository;
import com.myShop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addCartItem(User user,
                                Product product,
                                String size,
                                int quantity) {

        Cart cart=findUserCart(user);
        CartItem isPresent=cartItemRepository.findByCartAndProductAndSize(cart,product,size);

        if(isPresent==null)
        {
            CartItem cartItem=new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);

            int totalPrice=quantity*product.getSellingPrice();
            cartItem.setSellingPrice(totalPrice);
            cartItem.setMrpPrice(quantity*product.getMrpPrice());

            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);

            return cartItemRepository.save(cartItem);
        }

        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart=cartRepository.findByUserId(user.getId());

        int totalPrice=0;
        int totalDiscountPrice =0;
        int totalItem=0;

        for(CartItem cartItem:cart.getCartItems())
        {
            totalPrice+=cartItem.getMrpPrice();
            totalDiscountPrice +=cartItem.getSellingPrice();
            totalItem+=cartItem.getQuantity();
        }

        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItem(cart.getCartItems().size());
        cart.setTotalSellingPrice(totalDiscountPrice-cart.getCouponPrice());
        cart.setDiscount(calculateDiscountPercentage(totalPrice,totalDiscountPrice));
        cart.setTotalItem(totalItem);

        return cartRepository.save(cart);
    }


    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if (mrpPrice<=0)
        {
//            throw new IllegalArgumentException("Actual price must be greater then zero");
            return 0;
        }
        double discount=mrpPrice-sellingPrice;
        double discountPercentage=(discount/mrpPrice)*100;
        return (int) discountPercentage;
    }
}
