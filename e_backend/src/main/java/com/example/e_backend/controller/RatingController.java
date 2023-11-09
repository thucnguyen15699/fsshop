package com.example.e_backend.controller;

import com.example.e_backend.Model.Rating;
import com.example.e_backend.Model.User;
import com.example.e_backend.exception.ProductException;
import com.example.e_backend.request.RatingRequest;
import com.example.e_backend.service.RatingService;
import com.example.e_backend.service.UserException;
import com.example.e_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;

    @PostMapping("/create")
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest req,
                                               @RequestHeader("Authorization")String jwt)throws UserException, ProductException{
        User user=userService.findUserProfileByJwt(jwt);
        Rating rating=ratingService.createRating(req,user);
        return new ResponseEntity<Rating>(rating, HttpStatus.CREATED);
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>>getProductRating(@PathVariable Long productId,
                                                        @RequestHeader("Authorization")String jwt)throws UserException,ProductException{
        User user= userService.findUserProfileByJwt(jwt);
        List<Rating> ratings=ratingService.getProductRating(productId);

        return new ResponseEntity<>(ratings,HttpStatus.CREATED);
    }
}
