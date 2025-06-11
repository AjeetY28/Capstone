package com.myShop.service;

import com.myShop.domain.OrderStatus;
import com.myShop.entity.*;
import com.myShop.exceptions.OrderException;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);

    Order findOrderById(long id) throws OrderException;

    List<Order> userOrderHistory(Long userId);

//    List<Order> sellersOrder(Long sellerId);
    List<Order> getShopsOrders(Long sellerId);

    Order updateOrderStatus(Long orderId, OrderStatus orderStatus)
            throws OrderException;

    Order cancelOrder(Long orderId,User user) throws OrderException;

    void deleteOrder(Long orderId) throws OrderException;
    OrderItem getOrderItemById(Long id) throws OrderException;

}
