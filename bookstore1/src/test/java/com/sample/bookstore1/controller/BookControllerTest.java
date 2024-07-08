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

import com.sample.bookstore1.model.Book;
import com.sample.bookstore1.service.BookService;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private Book book;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setDescription("Test Description");
        book.setAddedDate(new Date());
        book.setGenre("Test Genre");
    }

    @Test
    void testAddBook() {
        when(bookService.addBook(any(Book.class))).thenReturn(book);

        ResponseEntity<Book> response = bookController.addBook(book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
        verify(bookService, times(1)).addBook(any(Book.class));
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(book);
        when(bookService.getAllBooks()).thenReturn(books);

        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById_Found() {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        ResponseEntity<Book> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookController.getBookById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void testUpdateBook_Found() {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));
        when(bookService.updateBook(eq(1L), any(Book.class))).thenReturn(book);

        ResponseEntity<?> response = bookController.updateBook(1L, book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
        verify(bookService, times(1)).getBookById(1L);
        verify(bookService, times(1)).updateBook(eq(1L), any(Book.class));
    }

    @Test
    void testUpdateBook_NotFound() {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookController.updateBook(1L, book);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book not found", response.getBody());
        verify(bookService, times(1)).getBookById(1L);
        verify(bookService, times(0)).updateBook(eq(1L), any(Book.class));
    }

    @Test
    void testDeleteBook_Found() {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        ResponseEntity<?> response = bookController.deleteBook(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Successfully", response.getBody());
        verify(bookService, times(1)).getBookById(1L);
        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookController.deleteBook(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book not found", response.getBody());
        verify(bookService, times(1)).getBookById(1L);
        verify(bookService, times(0)).deleteBook(1L);
    }
}