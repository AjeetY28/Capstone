package com.myShop.controller;

import com.myShop.domain.AccountStatus;
import com.myShop.entity.HomeCategory;
import com.myShop.entity.Seller;
import com.myShop.exceptions.SellerException;
import com.myShop.service.HomeCategoryService;
import com.myShop.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final SellerService sellerService;
    private final HomeCategoryService homeCategoryService;

    @PatchMapping("/seller/{id}/status/{status}")
    public ResponseEntity<Seller>updateSellerStatus(
            @PathVariable Long id,
            @PathVariable AccountStatus status
            )throws SellerException
    {
        Seller updatedSeller=sellerService.updateSellerAccountStatus(id,status);
        return ResponseEntity.ok(updatedSeller);
    }

    @GetMapping("/home-category")
    public ResponseEntity<List<HomeCategory>> getHomeCategory()
            throws Exception{

        List<HomeCategory> categories=homeCategoryService.getAllHomeCategories();
        return ResponseEntity.ok(categories);
    }

    @PatchMapping("/home-category/{id}")
    public ResponseEntity<HomeCategory>updateHomeCategory(
        @PathVariable Long id,
        @RequestBody HomeCategory homeCategory
    )throws Exception{
        HomeCategory updatedCategory=homeCategoryService.updateHomeCategory(homeCategory,id);
        return ResponseEntity.ok(updatedCategory);
    }

}
