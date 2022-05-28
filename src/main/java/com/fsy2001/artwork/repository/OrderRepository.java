package com.fsy2001.artwork.repository;

import com.fsy2001.artwork.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> getOrderByUsername(String username);
}
