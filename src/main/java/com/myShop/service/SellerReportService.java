package com.myShop.service;

import com.myShop.entity.Seller;
import com.myShop.entity.SellerReport;

public interface SellerReportService {

    SellerReport getSellerReport(Seller sellerId);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
