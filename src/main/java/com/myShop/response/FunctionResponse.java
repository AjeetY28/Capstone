package com.myShop.response;

import com.myShop.dto.OrderHistory;
import com.myShop.entity.Cart;
import com.myShop.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionResponse {

    private String functionName;
    private Cart userCart;
    private OrderHistory orderHistory;
    private Product product;
}
