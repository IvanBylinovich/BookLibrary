package com.solbeg.BookLibrary.repository;

import com.solbeg.BookLibrary.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {

    Optional<Tag> findTagByName(String name);
}
