package com.myShop.dto;


import lombok.Data;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.util.List;

@Data
public class OrderHistory {

    private Long id;
    private UserDto user;
    private List<SpringDataJaxb.OrderDto> currentOrders;
    private int totalOrders;
    private int cancelledOrders;
    private int completedOrders;
    private int pendingOrders;
}
