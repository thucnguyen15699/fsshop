package com.example.e_backend.repository;

import com.example.e_backend.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Order,Long> {

    @Query(" select  o FROM Order o WHERE o.user.id =:userId AND (o.orderStatus = 'PLACED' OR o.orderStatus = 'CONFIRMED' OR o.orderStatus= 'SHIPPED' or o.orderStatus= 'DELIVERED')")
    public List<Order> getUsersOrders(@Param("userId") Long userId);
}
