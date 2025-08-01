package com.myShop.service.impl;

import com.myShop.entity.Order;
import com.myShop.entity.Seller;
import com.myShop.entity.Transaction;
import com.myShop.repository.SellerRepository;
import com.myShop.repository.TransactionRepository;
import com.myShop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;


    @Override
    public Transaction createTransaction(Order order) {
        Seller seller=sellerRepository.findById(order.getSellerId()).get();

        Transaction transaction=new Transaction();
        transaction.setSeller(seller);
        transaction.setCustomer(order.getUser());
        transaction.setOrder(order);


        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsBySellerId(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
