package com.example.e_backend.controller;

import com.example.e_backend.Model.Review;
import com.example.e_backend.Model.User;
import com.example.e_backend.exception.ProductException;
import com.example.e_backend.request.ReviewRequest;
import com.example.e_backend.service.ReviewService;
import com.example.e_backend.service.UserException;
import com.example.e_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Review>createReview(@RequestBody ReviewRequest req,
                                              @RequestHeader("Authorization")String jwt)throws UserException, ProductException {
        User user=userService.findUserProfileByJwt(jwt);
        Review review=reviewService.createReview(req,user);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>>getProductReview(@PathVariable Long productId
                                                        )throws UserException,ProductException{
        List<Review>reviews=reviewService.getAllReview(productId);
        return new ResponseEntity<>(reviews,HttpStatus.ACCEPTED);
    }
}
