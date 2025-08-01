package com.myShop.service.impl;

import com.myShop.domain.PaymentOrderStatus;
import com.myShop.domain.PaymentStatus;
import com.myShop.entity.Order;
import com.myShop.entity.PaymentOrder;
import com.myShop.entity.User;
import com.myShop.repository.CartRepository;
import com.myShop.repository.OrderRepository;
import com.myShop.repository.PaymentOrderRepository;
import com.myShop.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    @Value("${razorpay.api.key}")
    private  String apiKey="apiKey";

    @Value("${razorpay.api.secret}")
    private  String apiSecret="apiSecret";

    @Value("${stripe.api.key}")
    private  String stripeSecretKey="stripeSecretKey";



    @Override
    public PaymentOrder createOrder(User user, Set<Order> orders) {
        Long amount=orders.stream().mapToLong(Order::getTotalSellingPrince).sum();
        int couponPrice=cartRepository.findByUserId(user.getId()).getCouponPrice();

        PaymentOrder paymentOrder=new PaymentOrder();
        paymentOrder.setAmount(amount-couponPrice);
        paymentOrder.setUser(user);
        paymentOrder.setOrders(orders);


        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {

        Optional<PaymentOrder> optionalPaymentOrder=paymentOrderRepository.findById(orderId);
        if(optionalPaymentOrder.isEmpty()){
            throw new Exception("Payment order not found with id "+orderId);
        }
        return optionalPaymentOrder.get();
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
    public PaymentLink createRazorpayPaymentLink(User user,
                                                 Long amount,
                                                 Long orderId)
            throws RazorpayException {

        amount=amount*100;

        try{
            RazorpayClient razorpay=new RazorpayClient(apiKey,apiSecret);
            JSONObject paymentLinkRequest=new JSONObject();
            paymentLinkRequest.put("amount",amount);
            paymentLinkRequest.put("currency","INR");

            JSONObject customer=new JSONObject();
            customer.put("name",user.getName());
            customer.put("email",user.getEmail());
            paymentLinkRequest.put("customer",customer);

            JSONObject notify=new JSONObject();
            notify.put("email",true);
            notify.put("sms",true);
            paymentLinkRequest.put("notify",notify);

            paymentLinkRequest.put("callback_url",
                    "http://localhost:3000/payment-success/"+orderId);

            paymentLinkRequest.put("callback_method","get");

            PaymentLink paymentLink=razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkUrl=paymentLink.get("short_url");
            String paymentLinkId=paymentLink.get("id");

            return paymentLink;

        }catch (RazorpayException e){
            System.out.println(e.getMessage());
            throw new RazorpayException(e.getMessage());
        }

    }

    @Override
    public String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/"+orderId)
                .setCancelUrl("http://localhost:3000/payment-cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount*100)
                                .setProductData(
                                        SessionCreateParams
                                                .LineItem
                                                .PriceData
                                                .ProductData
                                                .builder()
                                                .setName("Ecommerce")
                                                .build()
                                ).build()
                        ).build()
                ).build();

        Session session= Session.create(params);
        return session.getUrl();
    }
}
