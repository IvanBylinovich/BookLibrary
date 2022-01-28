package com.solbeg.BookLibrary.repository;

import com.solbeg.BookLibrary.model.entity.Author;
import com.solbeg.BookLibrary.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    Optional<Book> findBookByTitleAndAuthor(String title, Author author);

    Optional<List<Book>> findAllByAuthor(Author author);

    Optional<List<Book>> findAllByTags_Empty();
}
