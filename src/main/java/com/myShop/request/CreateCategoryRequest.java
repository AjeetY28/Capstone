package com.myShop.request;

import lombok.Data;

@Data
public class CreateCategoryRequest {

    private String parentCategoryId;
    private int level;
    private String name;
    private String categoryId;
}
