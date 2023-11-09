package com.example.e_backend.service;

import com.example.e_backend.Model.Address;
import com.example.e_backend.Model.Order;
import com.example.e_backend.Model.User;
import com.example.e_backend.exception.OrderException;

import java.util.List;

public interface OrderService {

    public Order creatOder(User user, Address shippaddress);

    public Order findOrderById(Long orderId) throws OrderException;
    public List<Order> usersOrderHistory(Long userId);
    public Order placedOrder(Long orderId) throws OrderException;
    public Order confirmedOrder(Long orderId) throws OrderException;
    public Order shippedOrder(Long orderId) throws OrderException;
    public Order deliveredOrder(Long orderId) throws OrderException;
    public Order cancledOrder(Long orderId) throws OrderException;

    public List<Order>getAllOrders();

    public void deleteOrder(Long orderId) throws OrderException;

}
