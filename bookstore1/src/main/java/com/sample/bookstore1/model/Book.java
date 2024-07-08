package com.sample.bookstore1.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String description;

    @Temporal(TemporalType.DATE)
    private Date addedDate;

    @NotBlank
    private String genre;

    /*
     * @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval =
     * true)
     * private List<Review> reviews;
     */
}