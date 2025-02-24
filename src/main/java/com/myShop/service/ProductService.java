package com.myShop.service;

import com.myShop.entity.Product;
import com.myShop.entity.Seller;
import com.myShop.request.CreateProductRequest;

public interface ProductService {

    public Product createProduct(CreateProductRequest req, Seller seller);
}
