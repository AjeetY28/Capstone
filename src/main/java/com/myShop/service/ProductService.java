package com.myShop.service;

import com.myShop.entity.Product;
import com.myShop.entity.Seller;
import com.myShop.exceptions.ProductException;
import com.myShop.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    Product createProduct(CreateProductRequest req, Seller seller)throws ProductException;
    void deleteProduct(Long productId) throws ProductException;
    Product updateProduct(Long productId, Product product) throws ProductException;
    Product updateProductStock(Long productId)throws ProductException;
    Product findProductById(Long productId) throws ProductException;
    List<Product> searchProduct(String query);
    Page<Product> getAllProduct(String Category,
                                       String brand,
                                       String color,
                                       String sizes,
                                       Integer minPrice,
                                       Integer maxPrice,
                                       Integer minDiscount,
                                       String sort,
                                       String stock,
                                       Integer pageNumber
                                       );
    List<Product> recentlyAddedProduct();
    List<Product> getProductsBySellerId(Long sellerId);
}
