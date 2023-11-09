package com.example.e_backend.service;

import com.example.e_backend.Model.Review;
import com.example.e_backend.Model.User;
import com.example.e_backend.exception.ProductException;
import com.example.e_backend.request.ReviewRequest;

import java.util.List;

public interface ReviewService {

    public Review createReview(ReviewRequest req, User user)throws ProductException;
    public List<Review> getAllReview(Long productId);


}
