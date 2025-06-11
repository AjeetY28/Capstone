package com.myShop.service;

import com.myShop.entity.Home;
import com.myShop.entity.HomeCategory;

import java.util.List;

public interface HomeService {

   Home createHomePageData(List<HomeCategory> allCategories);
}
