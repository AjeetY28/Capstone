package com.myShop.controller;

import com.myShop.entity.Product;
import com.myShop.entity.User;
import com.myShop.entity.WishList;
import com.myShop.exceptions.ProductException;
import com.myShop.exceptions.UserException;
import com.myShop.exceptions.WishlistNotFoundException;
import com.myShop.service.ProductService;
import com.myShop.service.UserService;
import com.myShop.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishList")
public class WishListController {

    private final WishListService wishListService;
    private final UserService userService;
    private final ProductService productService;


    @PostMapping("/create")
    public ResponseEntity<WishList> createWishlist(@RequestBody User user) {
        WishList wishlist = wishListService.createWishList(user);
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping
    public ResponseEntity<WishList> getWishListByUserId(
            @RequestHeader("Authorization") String jwt) throws UserException
    {
        User user=userService.findUserByJwtToken(jwt);
        WishList wishList=wishListService.getWishListByUserId(user);
        return ResponseEntity.ok(wishList);
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<WishList> addProductToWishList(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String jwt)throws WishlistNotFoundException, ProductException, UserException
    {
        Product product=productService.findProductById(productId);
        User user=userService.findUserByJwtToken(jwt);
        WishList updateWishList=wishListService.addProductToWishList(
                user,product);

        return ResponseEntity.ok(updateWishList);
    }

}
