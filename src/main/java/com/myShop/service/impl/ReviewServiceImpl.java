package com.myShop.service.impl;

import com.myShop.entity.Product;
import com.myShop.entity.Review;
import com.myShop.entity.User;
import com.myShop.exceptions.ReviewNotFoundException;
import com.myShop.repository.ReviewRepository;
import com.myShop.request.CreateReviewRequest;
import com.myShop.service.ProductService;
import com.myShop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductService productService;

    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {
        Review review=new Review();

        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProductImages(req.getProductImages());

        product.getReviews().add(review);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws ReviewNotFoundException, AuthenticationException {
        Review review=reviewRepository.findById(reviewId)
                .orElseThrow(()-> new ReviewNotFoundException("Review Not found"));

        if(review.getUser().getId()!=userId){
            throw new AuthenticationException("You do not have permission to delete this review");
        }

        review.setReviewText(reviewText);
        review.setRating(rating);
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws ReviewNotFoundException, AuthenticationException {
        Review review=reviewRepository.findById(reviewId)
                .orElseThrow(()-> new ReviewNotFoundException("Review Not found"));
        if(review.getUser().getId()!=userId){
            throw new AuthenticationException("You do not have permission to delete this review");
        }
        reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long reviewId) throws ReviewNotFoundException, AuthenticationException {
        return reviewRepository.findById(reviewId).orElseThrow(
                ()->new ReviewNotFoundException("Review not found"));
    }
}
