package com.myShop.controller;

import com.myShop.entity.Order;
import com.myShop.entity.Seller;
import com.myShop.entity.Transaction;
import com.myShop.exceptions.SellerException;
import com.myShop.service.SellerService;
import com.myShop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final SellerService sellerService;


    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Order order) {
        Transaction transaction = transactionService.createTransaction(order);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/seller")
    public ResponseEntity<List<Transaction>> getTransactionBySeller(
            @RequestHeader("Authorization") String jwt) throws SellerException {

        Seller seller=sellerService.getSellerProfile(jwt);

        List<Transaction> transactions=transactionService.getTransactionsBySellerId(seller);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>>getAllTransaction(){
        List<Transaction>transactions=transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }


}
