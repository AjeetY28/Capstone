package com.myShop.service.impl;

import com.myShop.entity.Product;
import com.myShop.entity.User;
import com.myShop.entity.WishList;
import com.myShop.repository.WishListRepository;
import com.myShop.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;


    @Override
    public WishList createWishList(User user) {
        WishList wishList=new WishList();
        wishList.setUser(user);
        return wishListRepository.save(wishList);
    }

    @Override
    public WishList getWishListByUserId(User user) {

        WishList wishList=wishListRepository.findByUserId(user.getId());
        if(wishList==null)
        {
            wishList=this.createWishList(user);
        }
        return wishList;
    }

    @Override
    public WishList addProductToWishList(User user, Product product) {
        WishList wishList=this.getWishListByUserId(user);
        if(wishList.getProduct().contains(product))
        {
            wishList.getProduct().remove(product);
        }
        else {
            wishList.getProduct().add(product);
        }
        return wishListRepository.save(wishList);
    }
}
