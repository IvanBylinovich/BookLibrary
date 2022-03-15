package com.solbeg.BookLibrary.repository;

import com.solbeg.BookLibrary.model.entity.Order;
import com.solbeg.BookLibrary.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<List<Order>> findAllByUser(User user);
}
