package com.myShop.controller;


import com.myShop.entity.Home;
import com.myShop.entity.HomeCategory;
import com.myShop.service.HomeCategoryService;
import com.myShop.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final HomeCategoryService homeCategoryService;
    private final HomeService homeService;


    @GetMapping("/home-page")
    public ResponseEntity<Home> getHomePageData()
    {
//        Home homePageData = homeService.getHomePageData();
//        return new ResponseEntity<>(homePageData, HttpStatus.ACCEPTED);
        return null;
    }

    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategories(
            @RequestBody List<HomeCategory> homeCategories
    ){
        List<HomeCategory> categories=homeCategoryService.createHomeCategories(homeCategories);
        Home home=homeService.createHomePageData(categories);
        return new ResponseEntity<>(home, HttpStatus.ACCEPTED);
    }
}
