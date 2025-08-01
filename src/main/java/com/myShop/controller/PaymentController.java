package com.myShop.controller;

import com.myShop.domain.PaymentMethod;
import com.myShop.entity.*;
import com.myShop.repository.CartRepository;
import com.myShop.response.ApiResponse;
import com.myShop.response.PaymentLinkResponse;
import com.myShop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final TransactionService transactionService;
    private final CartRepository cartRepository;


    @PostMapping("/{paymentMethod}/order/{orderId}")
    public ResponseEntity<PaymentLinkResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt
            )throws Exception{

        User user=userService.findUserByJwtToken(jwt);

        PaymentLinkResponse paymentResponse;

        PaymentOrder oder=paymentService.getPaymentOrderById(orderId);

        return new ResponseEntity<>(null,HttpStatus.CREATED);
    }


    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user=userService.findUserByJwtToken(jwt);
        PaymentLinkResponse paymentLinkResponse;

        PaymentOrder paymentOrder=paymentService.
                getPaymentOrderByPaymentId(paymentLinkId);

        boolean paymentSuccess=paymentService.ProceedPaymentOrder(
                paymentOrder,
                paymentId,
                paymentLinkId
        );

        if(paymentSuccess){
            for(Order order:paymentOrder.getOrders()){
                transactionService.createTransaction(order);
                Seller seller=sellerService.getSellerById(order.getSellerId());
                SellerReport report=sellerReportService.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders()+1);
                report.setTotalEarning(report.getTotalEarning()+order.getTotalSellingPrince());//check that
                report.setTotalSales(report.getTotalSales()+order.getOrderItems().size());
                sellerReportService.updateSellerReport(report);
            }
            Cart cart=cartRepository.findByUserId(user.getId());
            cart.setCouponPrice(0);
            cart.setCouponCode(null);
            cartRepository.save(cart);
        }

        ApiResponse res=new ApiResponse();
        res.setMessage("Payment Success");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

}
