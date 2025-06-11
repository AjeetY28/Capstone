package com.myShop.exceptions;

public class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException(String categoryNotFound) {
        super(categoryNotFound);
    }
}
