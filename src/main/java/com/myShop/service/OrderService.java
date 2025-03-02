package com.myShop.service;

import com.myShop.domain.OrderStatus;
import com.myShop.entity.Address;
import com.myShop.entity.Cart;
import com.myShop.entity.Order;
import com.myShop.entity.User;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
    Order findOrderById(long id) throws Exception;
    List<Order> userOrderHistory(Long userId);
    List<Order> sellersOrder(Long sellerId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;
    Order cancelOrder(Long orderId,User user) throws Exception;
}
