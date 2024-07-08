package com.sample.bookstore1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.bookstore1.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}