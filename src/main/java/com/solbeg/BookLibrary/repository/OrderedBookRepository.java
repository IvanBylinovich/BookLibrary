package com.solbeg.BookLibrary.repository;

import com.solbeg.BookLibrary.model.entity.OrderedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedBookRepository extends JpaRepository<OrderedBook, String> {

}
