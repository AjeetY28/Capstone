package com.myShop.repository;

import com.myShop.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {


    List<Transaction> findBySellerId(Long sellerId);
}
