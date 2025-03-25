package com.myShop.controller;

import com.myShop.entity.Deal;
import com.myShop.response.ApiResponse;
import com.myShop.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/deals")
public class DealController {

    private final DealService dealService;

    @PostMapping
    public ResponseEntity<Deal> createDeal(@RequestBody Deal deals)
    {
        Deal createdDeals =dealService.createDeal(deals);

        return new ResponseEntity<>(createdDeals, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Deal>updateDeal(
            @PathVariable Long id,
            @RequestBody Deal deals) throws Exception
    {
        Deal updatedDeals=dealService.updateDeal(deals,id);
        return ResponseEntity.ok(updatedDeals);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDeal(
            @PathVariable Long id) throws Exception{

        dealService.deleteDeal(id);

        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage("Deal deleted successfully");


        return new ResponseEntity<>(apiResponse,HttpStatus.ACCEPTED);
    }

}
