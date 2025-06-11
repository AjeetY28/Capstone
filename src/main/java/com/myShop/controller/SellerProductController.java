package com.myShop.controller;


import com.myShop.entity.Product;
import com.myShop.entity.Seller;
import com.myShop.exceptions.CategoryNotFoundException;
import com.myShop.exceptions.ProductException;
import com.myShop.exceptions.SellerException;
import com.myShop.exceptions.UserException;
import com.myShop.request.CreateProductRequest;
import com.myShop.service.ProductService;
import com.myShop.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/products")
public class SellerProductController {

    private final ProductService productService;
    private final SellerService sellerService;

    @GetMapping()
    public ResponseEntity<List<Product>> getProductBySellerId(
            @RequestHeader("Authorization") String jwt)
            throws SellerException,ProductException {
        Seller seller = sellerService.getSellerProfile(jwt);

        List<Product> products = productService.getProductsBySellerId(seller.getId());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Product> createProduct(
            @RequestBody CreateProductRequest request,
            @RequestHeader("Authorization") String jwt) throws UserException,
            ProductException, CategoryNotFoundException, SellerException {


        Seller seller = sellerService.getSellerProfile(jwt);
        Product product = productService.createProduct(request, seller);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId) {

        try {
            productService.deleteProduct(productId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProductException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product product) throws ProductException
        {
            try {
                Product updatedProduct = productService.updateProduct(productId, product);
                return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
            } catch (ProductException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Product>updateProductStock(
            @PathVariable Long productId
    ){
        try{
            Product updatedProduct =productService.updateProductStock(productId);
            return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
        }catch (ProductException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
