package com.myShop.service.impl;

import com.myShop.domain.PaymentOrderStatus;
import com.myShop.domain.PaymentStatus;
import com.myShop.entity.Order;
import com.myShop.entity.PaymentOrder;
import com.myShop.entity.User;
import com.myShop.repository.OrderRepository;
import com.myShop.repository.PaymentOrderRepository;
import com.myShop.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private PaymentOrderRepository paymentOrderRepository;
    private OrderRepository orderRepository;

    private String apiKey="apikey";
    private String apiSecret="apisecret";


    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        Long amount=orders.stream().mapToLong(Order::getTotalSellingPrince).sum();

        PaymentOrder paymentOrder=new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);
        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {

        return paymentOrderRepository.findById(orderId)
                .orElseThrow(()->new Exception("Payment order not found"));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception {
        PaymentOrder paymentOrder=paymentOrderRepository.findByPaymentLinkId(orderId);
        if(paymentOrder==null){
            throw new Exception("payment order not found with provided payment link id");
        }
        return paymentOrder;
    }

    @Override
    public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder,
                                       String paymentId,
                                       String paymentLinkedId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            RazorpayClient razorpay=new RazorpayClient(apiKey,apiSecret);

            Payment payment=razorpay.payments.fetch(paymentId);

            String status=payment.get("status");

            if(status.equals("captured")){
                Set<Order> orders=paymentOrder.getOrders();
                for(Order order:orders){
                    order.setPaymentStatus(PaymentStatus.COMPLETED);
                    orderRepository.save(order);
                }
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;
            }
            paymentOrder.setStatus(PaymentOrderStatus.FAILED);
            paymentOrderRepository.save(paymentOrder);
            return false;

        }
        return false;
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(User user, Long amount, Long orderId) {
        return null;
    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) {
        return "";
    }
}
