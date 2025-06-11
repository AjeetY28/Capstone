package com.myShop.controller;


import com.myShop.entity.Cart;
import com.myShop.entity.CartItem;
import com.myShop.entity.Product;
import com.myShop.entity.User;
import com.myShop.exceptions.CartItemException;
import com.myShop.exceptions.ProductException;
import com.myShop.exceptions.UserException;
import com.myShop.request.AddItemRequest;
import com.myShop.response.ApiResponse;
import com.myShop.service.CartItemService;
import com.myShop.service.CartService;
import com.myShop.service.ProductService;
import com.myShop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(
            @RequestHeader("Authorization") String jwt)
            throws UserException {

        User user=userService.findUserByJwtToken(jwt);

        Cart cart=cartService.findUserCart(user);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem>addItemToCart(
            @RequestBody AddItemRequest req,
            @RequestHeader("Authorization") String jwt
            ) throws ProductException,UserException{

        User user=userService.findUserByJwtToken(jwt);

        Product product=productService.findProductById(req.getProductId());

        CartItem item=cartService.addCartItem(
                user,
                product,
                req.getSize(),
                req.getQuantity());

        ApiResponse res=new ApiResponse();
        res.setMessage("Item added to cart successfully");
        return new ResponseEntity<>(item,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse>deleteCartItemHandler(
        @PathVariable Long cartItemId,
        @RequestHeader("Authorization") String jwt
    )throws CartItemException ,UserException{
        User user=userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(),cartItemId);


        ApiResponse res=new ApiResponse();
        res.setMessage("Item removed from cart successfully");
        return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);
    }

    @PutMapping("item/{cartItemId}")
    public ResponseEntity<CartItem>updateCartHandler(
        @PathVariable Long cartItemId,
        @RequestBody CartItem cartItem,
        @RequestHeader("Authorization") String jwt
    ) throws UserException,CartItemException{

        User user=userService.findUserByJwtToken(jwt);

        CartItem updatedCartItem=null;
        if(cartItem.getQuantity()>0){
            updatedCartItem=cartItemService.updateCartItem(
                    user.getId(),
                    cartItemId,
                    cartItem);
        }

        return new ResponseEntity<>(updatedCartItem,HttpStatus.ACCEPTED);
    }


}
