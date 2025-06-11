package com.myShop.controller;

import com.myShop.domain.OrderStatus;
import com.myShop.entity.Order;
import com.myShop.entity.Seller;
import com.myShop.exceptions.OrderException;
import com.myShop.exceptions.SellerException;
import com.myShop.response.ApiResponse;
import com.myShop.service.OrderService;
import com.myShop.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/orders")
public class SellerOrderController {

    private final SellerService sellerService;
    private final OrderService orderService;

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrderHandler(
            @RequestHeader("Authorization") String jwt
    ) throws SellerException {
        Seller seller=sellerService.getSellerProfile(jwt);
        List<Order> orders=orderService.getShopsOrders(seller.getId());

        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatusHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId,
            @PathVariable OrderStatus orderStatus)
        throws OrderException {

        Order order=orderService.updateOrderStatus(orderId,orderStatus);
        return new ResponseEntity<>(order,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ApiResponse>deleteOrderHandler(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws OrderException{

        orderService.deleteOrder(orderId);
        ApiResponse res=new ApiResponse("Order Deleted Successfully",true);

        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }
}
