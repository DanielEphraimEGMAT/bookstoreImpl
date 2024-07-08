package com.sample.bookstore1.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.bookstore1.model.Book;
import com.sample.bookstore1.repository.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Add a new book
    public Book addBook(Book book) {
        book.setAddedDate(new Date());
        return bookRepository.save(book);
    }

    // Get all books with associated reviews
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get book by ID with associated reviews
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    // Update an existing book
    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setDescription(bookDetails.getDescription());
        book.setAddedDate(bookDetails.getAddedDate());
        book.setGenre(bookDetails.getGenre());
        return bookRepository.save(book);
    }

    // Delete a book by ID with associated reviews
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}