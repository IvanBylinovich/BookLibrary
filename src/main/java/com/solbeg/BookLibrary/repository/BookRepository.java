package com.solbeg.BookLibrary.repository;

import com.solbeg.BookLibrary.entity.Author;
import com.solbeg.BookLibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookByTitleAndAuthor(String title, Author author);
    boolean existsBookByTitleAndAuthor(String title, Author author);

}
