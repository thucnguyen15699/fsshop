package com.example.e_backend.service;

import com.example.e_backend.Model.Rating;
import com.example.e_backend.Model.User;
import com.example.e_backend.exception.ProductException;
import com.example.e_backend.request.RatingRequest;
import org.springframework.stereotype.Service;

import java.util.List;


public interface RatingService {
    public Rating createRating(RatingRequest req, User user) throws ProductException;

    public List<Rating> getProductRating(Long productId);


}
