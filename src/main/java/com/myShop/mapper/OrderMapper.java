package com.myShop.mapper;

import com.myShop.domain.OrderStatus;
import com.myShop.dto.OrderDto;
import com.myShop.dto.OrderHistory;
import com.myShop.dto.OrderItemDto;
import com.myShop.entity.Order;
import com.myShop.entity.OrderItem;
import com.myShop.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderItemDto toOrderItemDto(OrderItem orderItem){
        if(orderItem==null){
            return null;
        }

        OrderItemDto orderItemDto=new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setProduct(ProductMapper.toProductDto(orderItem.getProduct()));
        orderItemDto.setSize(orderItem.getSize());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setMrpPrice(orderItem.getMrpPrice());
        orderItemDto.setSellingPrice(orderItem.getSellingPrice());

        return orderItemDto;
    }

    // Maps OrderItemDto to OrderItem
    public static OrderItem toOrderItem(OrderItemDto orderItemDto){
        if (orderItemDto == null) {
            return null;
        }

        OrderItem orderItem=new OrderItem();
        orderItem.setId(orderItemDto.getId());
        orderItem.setSize(orderItemDto.getSize());
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setMrpPrice(orderItemDto.getMrpPrice());
        orderItem.setSellingPrice(orderItemDto.getSellingPrice());

        return orderItem;
    }

    //Maps Order to OrderDto
    public static OrderDto toOrderDto(Order order){
        if (order == null) {
            return null;
        }

        OrderDto orderDto=new OrderDto();

        orderDto.setId(order.getId());
        orderDto.setOrderId(order.getOrderId());
        orderDto.setUser(UserMapper.toUserDto(order.getUser()));
        orderDto.setSellerId(order.getSellerId());
        orderDto.setOrderItems(order.getOrderItems().stream().map(OrderMapper::toOrderItemDto).collect(Collectors.toList()));
        orderDto.setShippingAddress(order.getShippingAddress());
        orderDto.setPaymentDetails(order.getPaymentDetails());
        orderDto.setTotalMrpPrice(order.getTotalMrpPrice());
        orderDto.setTotalSellingPrince(order.getTotalSellingPrince());
        orderDto.setDiscount(order.getDiscount());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setTotalItem(order.getTotalItem());
        orderDto.setPaymentStatus(order.getPaymentStatus());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setDeliverDate(order.getDeliverDate());


        return orderDto;
    }
    //Maps OrderDto to Order
    public static Order toOrder(OrderDto orderDto){
        if (orderDto == null) {
            return null;
        }

        Order order=new Order();

        order.setId(orderDto.getId());
        order.setOrderId(orderDto.getOrderId());
//        order.setUser(UserMapper.toUser(orderDto.getUser()));
        order.setSellerId(orderDto.getSellerId());
//        order.setOrderItems(orderDto.getOrderItems().stream().map(OrderMapper::toOrderItem).collect(Collectors.toList()));
        order.setShippingAddress(orderDto.getShippingAddress());
        order.setPaymentDetails(orderDto.getPaymentDetails());
        order.setTotalMrpPrice(orderDto.getTotalMrpPrice());
        order.setTotalSellingPrince(orderDto.getTotalSellingPrince());
        order.setDiscount(orderDto.getDiscount());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setTotalItem(orderDto.getTotalItem());
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setOrderDate(orderDto.getOrderDate());
        order.setDeliverDate(orderDto.getDeliverDate());

        return order;
    }

    //Maps OderHistory to OrderHistoryDto
    public static OrderHistory toOrderHistory(List<Order> orders, User user) {
        if (orders == null) {
            return null;
        }

        OrderHistory orderHistory = new OrderHistory();
        //Set user
        orderHistory.setUser(UserMapper.toUserDto(user));

        // Filter current orders (those that are not DELIVERED or CANCELLED)
        List<OrderDto> currentOrders = orders.stream()
                .filter(order->order.getOrderStatus() != OrderStatus.DELIVERED && order.getOrderStatus() != OrderStatus.CANCELLED)
                .map(OrderMapper::toOrderDto)
                .collect(Collectors.toList());

        orderHistory.setCurrentOrders(currentOrders);

        //Set total Orders
        orderHistory.setTotalOrders(orders.size());

        //Set cancelled orders
        int cancelledOrders=(int)orders.stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.CANCELLED)
                .count();
        orderHistory.setCancelledOrders(cancelledOrders);

        //Set completed orders(those that are DELIVERED)
        int completedOrders=(int)orders.stream()
                .filter(order -> order.getOrderStatus() == OrderStatus.DELIVERED)
                .count();
        orderHistory.setCompletedOrders(completedOrders);

        return orderHistory;
    }
}
