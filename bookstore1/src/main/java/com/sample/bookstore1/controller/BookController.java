package com.sample.bookstore1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.bookstore1.model.Book;
import com.sample.bookstore1.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Book API's", description = "Provides API's for creating, updating, deleting and fetching books")
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Add a new book
    @Operation(summary = "Add Book", description = "Add a book to database", tags = { "addBook", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = BookController.class), mediaType = "application/json") })
    })
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    // Get all books with associated reviews
    @Operation(summary = "Fetch All Books", description = "Fetch all the books in database", tags = { "fetchBooks",
            "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = BookController.class), mediaType = "application/json") })
    })
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // Get book by ID with associated reviews
    @Operation(summary = "Fetch Book", description = "Fetch a book by ID", tags = { "fetchBook", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = BookController.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update an existing book
    @Operation(summary = "Update Book", description = "Update a book by ID", tags = { "updateBook", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = BookController.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        if (bookService.getBookById(id).isPresent()) {
            return ResponseEntity.ok(bookService.updateBook(id, bookDetails));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }

    }

    // Delete a book by ID with associated reviews
    @Operation(summary = "Delete Book", description = "Delete a book by ID", tags = { "deleteBook", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        if (bookService.getBookById(id).isPresent()) {
            bookService.deleteBook(id);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
    }
}