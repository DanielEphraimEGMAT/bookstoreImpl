package com.sample.bookstore1.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.bookstore1.model.Review;
import com.sample.bookstore1.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Add a new review
    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    // Get all reviews
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // Get review by ID
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    // Update an existing review
    public Review updateReview(Long id, Review reviewDetails) {
        Review review = reviewRepository.findById(id).orElseThrow();
        review.setRating(reviewDetails.getRating());
        review.setComment(reviewDetails.getComment());
        review.setTimestamp(new Date());
        return reviewRepository.save(review);
    }

    // Delete a review by ID
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    // Get reviews by user and book ID
    public Review getReviewsByUserAndBook(int userId, Long bookId) {
        return reviewRepository.findByUserIdAndBookId(userId, bookId);
    }
}