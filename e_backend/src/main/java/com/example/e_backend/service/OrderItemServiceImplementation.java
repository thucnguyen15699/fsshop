package com.example.e_backend.service;

import com.example.e_backend.Model.OrderItem;
import com.example.e_backend.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImplementation implements OrderItemService{
    @Autowired
    private OrderItemRepository orderItemRepository;


    public OrderItemServiceImplementation(OrderItemRepository orderItemRepository) {
        this.orderItemRepository=orderItemRepository;
    }

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
}
