package com.myShop.controller;


import com.myShop.service.RevenueService;
import com.myShop.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/revenue/chart")
public class RevenueController {

    private final RevenueService revenueService;
    private final SellerService sellerService;
}
