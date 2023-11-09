package com.example.e_backend.service;

import com.example.e_backend.Model.Product;
import com.example.e_backend.Model.Rating;
import com.example.e_backend.Model.User;
import com.example.e_backend.exception.ProductException;
import com.example.e_backend.repository.RatingRepository;
import com.example.e_backend.request.RatingRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImplementation implements RatingService{

    private RatingRepository ratingRepository;
    private ProductService productService;

    public RatingServiceImplementation(RatingRepository ratingRepository,ProductService productService) {
        this.ratingRepository=ratingRepository;
        this.productService=productService;
    }


    @Override
    public Rating createRating(RatingRequest req, User user) throws ProductException {
        Product product=productService.findProductById(req.getProductId());
        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(req.getRating());
        rating.setCreateAt(LocalDateTime.now());

        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getProductRating(Long productId) {

        return ratingRepository.getAllProductsRating(productId);

    }
}
