package com.solbeg.BookLibrary.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String imageUrl;
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    private Tag tag;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public Book(String title, String imageUrl, BigDecimal price, Tag tag) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.price = price;
        this.tag = tag;
    }
}
