package com.example.e_backend.service;

import com.example.e_backend.Model.Cart;
import com.example.e_backend.Model.CartItem;
import com.example.e_backend.Model.Product;
import com.example.e_backend.exception.CartItemException;

public interface CartItemService {
    public CartItem createCartItem (CartItem cartItem);

    public CartItem updateCartItem(Long userId,Long id, CartItem cartItem) throws CartItemException,UserException;

    public CartItem isCartItemExist(Cart cart, Product product, String size,Long userId);

    public void removeCartItem(Long userId,Long cartItemId) throws  CartItemException, UserException;

    public CartItem findCartItemById(Long cartItemId) throws CartItemException;

}
