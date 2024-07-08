package com.sample.bookstore1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.bookstore1.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByBookIdAndId(Long bookId, Long reviewId);

    Review findByUserIdAndBookId(int userId, Long bookId);
}