package com.myShop.repository;

import com.myShop.entity.Order;
import com.myShop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
