package com.myShop.service;

import com.myShop.entity.Product;
import com.myShop.entity.Review;
import com.myShop.entity.User;
import com.myShop.exceptions.ReviewNotFoundException;
import com.myShop.request.CreateReviewRequest;

import javax.naming.AuthenticationException;
import java.util.List;

public interface ReviewService {

    Review createReview(CreateReviewRequest req,
                        User user,
                        Product product);
    List<Review> getReviewByProductId(Long productId);

    Review updateReview(Long reviewId,String reviewText,double rating,Long userId) throws ReviewNotFoundException, AuthenticationException;
    void deleteReview(Long reviewId,Long userId) throws Exception;
    Review getReviewById(Long reviewId) throws Exception;
}
