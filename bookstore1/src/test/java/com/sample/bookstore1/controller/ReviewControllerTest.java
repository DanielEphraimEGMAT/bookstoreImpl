package com.sample.bookstore1.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sample.bookstore1.model.Book;
import com.sample.bookstore1.model.Review;
import com.sample.bookstore1.model.User;
import com.sample.bookstore1.service.BookService;
import com.sample.bookstore1.service.ReviewService;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private BookService bookService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ReviewController reviewController;

    private Review review;
    private Book book;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        review = new Review();
        review.setId(1L);
        review.setRating(4);
        review.setComment("Great Book");
        review.setTimestamp(new Date());
        review.setBook(book);
        review.setUser(user);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testAddReview_ValidReview() {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));
        when(reviewService.addReview(any(Review.class))).thenReturn(review);
        when(reviewService.getReviewsByUserAndBook(1, 1L)).thenReturn(null);

        ResponseEntity<?> response = reviewController.addReview(review);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(review, response.getBody());
        verify(reviewService, times(1)).addReview(any(Review.class));
    }

    @Test
    void testAddReview_RatingOutOfBounds() {
        review.setRating(6);

        ResponseEntity<?> response = reviewController.addReview(review);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ratings should be between 1 and 5", response.getBody());
        verify(reviewService, times(0)).addReview(any(Review.class));
    }

    @Test
    void testAddReview_UserAlreadyReviewed() {
        when(reviewService.getReviewsByUserAndBook(1, 1L)).thenReturn(review);

        ResponseEntity<?> response = reviewController.addReview(review);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User has already provided a rating for this book. Update the rating instead", response.getBody());
        verify(reviewService, times(0)).addReview(any(Review.class));
    }

    @Test
    void testAddReview_BookNotFound() {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = reviewController.addReview(review);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book with id: 1 does not exist", response.getBody());
        verify(reviewService, times(0)).addReview(any(Review.class));
    }

    @Test
    void testGetAllReviews() {
        List<Review> reviews = Arrays.asList(review);
        when(reviewService.getAllReviews()).thenReturn(reviews);

        ResponseEntity<List<Review>> response = reviewController.getAllReviews();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviews, response.getBody());
        verify(reviewService, times(1)).getAllReviews();
    }

    @Test
    void testGetReviewById_Found() {
        when(reviewService.getReviewById(1L)).thenReturn(Optional.of(review));

        ResponseEntity<Review> response = reviewController.getReviewById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(review, response.getBody());
        verify(reviewService, times(1)).getReviewById(1L);
    }

    @Test
    void testGetReviewById_NotFound() {
        when(reviewService.getReviewById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Review> response = reviewController.getReviewById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(reviewService, times(1)).getReviewById(1L);
    }

    @Test
    void testUpdateReview_Found() {
        when(reviewService.getReviewById(1L)).thenReturn(Optional.of(review));
        when(reviewService.updateReview(eq(1L), any(Review.class))).thenReturn(review);

        ResponseEntity<?> response = reviewController.updateReview(1L, review);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(review, response.getBody());
        verify(reviewService, times(1)).getReviewById(1L);
        verify(reviewService, times(1)).updateReview(eq(1L), any(Review.class));
    }

    @Test
    void testUpdateReview_NotFound() {
        when(reviewService.getReviewById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = reviewController.updateReview(1L, review);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Review with id:1 does not exist", response.getBody());
        verify(reviewService, times(1)).getReviewById(1L);
        verify(reviewService, times(0)).updateReview(eq(1L), any(Review.class));
    }

    @Test
    void testUpdateReview_RatingOutOfBounds() {
        review.setRating(6);

        ResponseEntity<?> response = reviewController.updateReview(1L, review);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ratings should be between 1 and 5", response.getBody());
        verify(reviewService, times(0)).updateReview(eq(1L), any(Review.class));
    }

    @Test
    void testDeleteReview_Found() {
        when(reviewService.getReviewById(1L)).thenReturn(Optional.of(review));

        ResponseEntity<?> response = reviewController.deleteReview(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Successfully", response.getBody());
        verify(reviewService, times(1)).getReviewById(1L);
        verify(reviewService, times(1)).deleteReview(1L);
    }

    @Test
    void testDeleteReview_NotFound() {
        when(reviewService.getReviewById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = reviewController.deleteReview(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Review with id:1 does not exist", response.getBody());
        verify(reviewService, times(1)).getReviewById(1L);
        verify(reviewService, times(0)).deleteReview(1L);
    }
}