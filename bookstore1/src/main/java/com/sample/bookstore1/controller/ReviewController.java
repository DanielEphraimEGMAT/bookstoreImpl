package com.sample.bookstore1.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.bookstore1.model.Review;
import com.sample.bookstore1.model.User;
import com.sample.bookstore1.service.BookService;
import com.sample.bookstore1.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Book Review API's", description = "Provides API's for creating, updating, deleting and fetching reviews")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookService bookService;

    // Add a new review
    @Operation(summary = "Add Review", description = "Add review for a book", tags = { "addReview", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ReviewController.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody Review review) {
        // checking if rating is between 1 and 5
        if (review.getRating() < 1 || review.getRating() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ratings should be between 1 and 5");
        }
        // Fetching user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        // checking if user has already reviewed the book
        if (reviewService.getReviewsByUserAndBook(currentUser.getId(), review.getBook().getId()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User has already provided a rating for this book. Update the rating instead");
        }
        // checking if the book exists
        if (!bookService.getBookById(review.getBook().getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book with id: " + review.getBook().getId() + " does not exist");
        }

        review.setUser(currentUser);
        review.setTimestamp(new Date());
        return ResponseEntity.ok(reviewService.addReview(review));
    }

    // Get all reviews
    @Operation(summary = "Get All Reviews", description = "Get all book reviews", tags = { "getAllReview", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ReviewController.class), mediaType = "application/json") })
    })
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    // Get review by ID
    @Operation(summary = "Get Review", description = "Get Review By Id", tags = { "getReview", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ReviewController.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update an existing review
    @Operation(summary = "Update Review", description = "Update review for a book by id", tags = { "updateReview",
            "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ReviewController.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody Review reviewDetails) {
        // checking if the review exists
        if (reviewService.getReviewById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review with id:" + id + " does not exist");
        }
        // checking if rating is between 1 and 5
        if (reviewDetails.getRating() < 1 || reviewDetails.getRating() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ratings should be between 1 and 5");
        }
        return ResponseEntity.ok(reviewService.updateReview(id, reviewDetails));
    }

    // Delete a review by ID
    @Operation(summary = "Delete Review", description = "Delete review for a book", tags = { "deleteReview", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        // checking if the review exists
        if (reviewService.getReviewById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review with id:" + id + " does not exist");
        } else {
            reviewService.deleteReview(id);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully");
        }
    }
}