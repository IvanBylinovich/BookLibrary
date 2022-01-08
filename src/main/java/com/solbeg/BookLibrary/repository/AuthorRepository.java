package com.solbeg.BookLibrary.repository;

import com.solbeg.BookLibrary.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findAuthorById(long id);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
