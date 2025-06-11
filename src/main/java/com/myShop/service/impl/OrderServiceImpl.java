package com.myShop.service.impl;

import com.myShop.domain.OrderStatus;
import com.myShop.domain.PaymentStatus;
import com.myShop.entity.*;
import com.myShop.exceptions.OrderException;
import com.myShop.repository.AddressRepository;
import com.myShop.repository.OrderItemRepository;
import com.myShop.repository.OrderRepository;
import com.myShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {

        if(!user.getAddresses().contains(shippingAddress))
        {
            user.getAddresses().add(shippingAddress);
        }
        Address address=addressRepository.save(shippingAddress);
//        brand1 => 4 shirts
//        brand2 => 2 paints
//        brand3 => 1 watch

        Map<Long , List<CartItem>> itemsBySeller=cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item->item.getProduct()
                        .getSeller().getId()));

        Set<Order> orders=new HashSet<>();

        for(Map.Entry<Long,List<CartItem>>entry:itemsBySeller.entrySet()){
            Long sellerId= entry.getKey();
            List<CartItem>items=entry.getValue();


            int totalOrderPrice=items.stream().
                    mapToInt(CartItem::getSellingPrice).sum();
            int totalItem=items.stream().mapToInt(CartItem::getQuantity).sum();

            Order createdOrder=new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalSellingPrince(totalOrderPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.setShippingAddress(address);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);


            Order savedOrder=orderRepository.save(createdOrder);
            orders.add(savedOrder);

            List<OrderItem> orderItems=new ArrayList<>();

            for(CartItem item:items)
            {
                OrderItem orderItem=new OrderItem();

                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setSellingPrice(item.getSellingPrice());


                savedOrder.getOrderItems().add(orderItem);

                OrderItem savedOrderItem=orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
            }
        }

        return orders;
    }

    @Override
    public Order findOrderById(long id) throws OrderException {

        Optional<Order> opt=orderRepository.findById(id);

        if(opt.isPresent()) {
            return opt.get();
        }
        throw new OrderException("order not exist with id "+id);
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {

        return orderRepository.findByUserId(userId);
    }

//    @Override
//    public List<Order> sellersOrder(Long sellerId) {
//        return orderRepository.findBySellerId(sellerId);
//    }
    @Override
    public List<Order> getShopsOrders(Long sellerId) {

    return orderRepository.findBySellerIdOrderByOrderDateDesc(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus)
            throws OrderException {
        Order order=findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, User user) throws OrderException {
        Order order=this.findOrderById(orderId);

        if(!order.getUser().getId().equals(user.getId()))
        {
            throw new OrderException("You don't have access to cancel this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);

        orderRepository.deleteById(orderId);

    }

    @Override
    public OrderItem getOrderItemById(Long id) throws OrderException {
        Optional<OrderItem> orderItem = orderItemRepository.findById(id);
        if(orderItem.isPresent()){
            return orderItem.get();
        }
        throw new OrderException("Order item not found");
    }
}
