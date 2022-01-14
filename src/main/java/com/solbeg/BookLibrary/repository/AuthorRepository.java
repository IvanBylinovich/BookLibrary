package com.solbeg.BookLibrary.repository;

import com.solbeg.BookLibrary.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, String> {

    Optional<Author> findAuthorById(String id);

    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
