package com.example.e_backend.repository;

import com.example.e_backend.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository  extends JpaRepository<OrderItem,Long> {

}
