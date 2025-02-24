package com.myShop.service;

import com.myShop.entity.Product;
import com.myShop.entity.Seller;
import com.myShop.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Product createProduct(CreateProductRequest req, Seller seller);
    public void deleteProduct(Long productId);
    public Product updateProduct(Long productId, Product product);
    public Product getProductById(Long productId);
    List<Product> searchProduct();
    public Page<Product> getAllProduct(String Category,
                                       String brand,
                                       String colors,
                                       String sizes,
                                       Integer minPrice,
                                       Integer maxPrice,
                                       Integer minDiscount,
                                       String sort,
                                       String stock,
                                       Integer pageNumber
                                       );
    List<Product> getProductsBySellerId(Long sellerId);
}
