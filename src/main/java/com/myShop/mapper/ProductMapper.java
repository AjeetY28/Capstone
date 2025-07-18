package com.myShop.mapper;

import com.myShop.dto.ProductDto;
import com.myShop.entity.Product;

public class ProductMapper {

    public static ProductDto toProductDto(Product product){

        ProductDto productDto=new ProductDto();

        productDto.setId(product.getId());

        productDto.setTitle(product.getTitle());

        productDto.setDescription(product.getDescription());

        productDto.setMrpPrice(product.getMrpPrice());

        productDto.setSellingPrice(product.getSellingPrice());

        productDto.setDiscountPercent(product.getDiscountPercent());

        productDto.setQuantity(product.getQuantity());

        productDto.setColor(product.getColor());

        productDto.setImages(product.getImages());

        productDto.setNumRatings(product.getNumRatings());

        productDto.setSizes(product.getSizes());

        productDto.setCreatedAt(product.getCreatedAt());


        return productDto;

    }

    public Product mapTOEntity(ProductDto productDto){
        return null;
    }
}
