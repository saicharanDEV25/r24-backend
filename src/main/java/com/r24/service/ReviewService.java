package com.r24.service;

import com.r24.entity.Review;

import java.util.List;

public interface ReviewService {

    Review addReview(Review review);

    List<Review> getAllReviews();

    Review getReviewById(Long id);

    void deleteReview(Long id);
}
