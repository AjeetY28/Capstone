package com.myShop.entity;


import com.myShop.domain.PaymentStatus;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {

    private String paymentId;
    private String razorpayPaymentLinkId;
    private String razorpayPaymentLinkReferenceId;
    private String razorpayPaymentLinkStatus;
//    private String razorpayPaymentId; //ZWSP will be not added it will be added by razorpay
    private String razorpayPaymentId​;
    private PaymentStatus status;
}
