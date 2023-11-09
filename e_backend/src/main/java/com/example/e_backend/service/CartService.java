package com.example.e_backend.service;

import com.example.e_backend.Model.Cart;
import com.example.e_backend.Model.User;
import com.example.e_backend.exception.ProductException;
import com.example.e_backend.request.AddItemRequest;

public interface CartService {
    public Cart createCart(User user);

    public String addCartItem(Long userId, AddItemRequest req) throws ProductException;

    public Cart findUserCart(Long userId);


}
