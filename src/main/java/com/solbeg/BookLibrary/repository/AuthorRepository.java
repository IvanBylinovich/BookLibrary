package com.solbeg.BookLibrary.repository;

import com.solbeg.BookLibrary.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findAuthorById(String id);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
