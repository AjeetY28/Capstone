package com.myShop.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(SellerException.class)
    public ResponseEntity<ErrorDetails> sellerExceptionHandler(SellerException se, WebRequest req) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(se.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(java.time.LocalDateTime.now());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDetails> ProductExceptionHandler(SellerException se, WebRequest req) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(se.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(java.time.LocalDateTime.now());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetails> UserExceptionHandler(UserException ue,WebRequest req){

//        ErrorDetails err=new ErrorDetails(ue.getMessage(), ,req.getDescription(false), LocalDateTime.now());

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(ue.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(java.time.LocalDateTime.now());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CartItemException.class)
    public ResponseEntity<ErrorDetails> CartItemExceptionHandler(CartItemException cie,WebRequest req){

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(cie.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(java.time.LocalDateTime.now());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorDetails> OrderExceptionHandler(OrderException oe,WebRequest req){

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(oe.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(java.time.LocalDateTime.now());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SellerException.class)
    public ResponseEntity<ErrorDetails> SellerExceptionHandler(SellerException se,WebRequest req){

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(se.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(java.time.LocalDateTime.now());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CouponNotValidException.class)
    public ResponseEntity<ErrorDetails> CouponExceptionHandler(CouponNotValidException cne,WebRequest req){

        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(cne.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(java.time.LocalDateTime.now());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
