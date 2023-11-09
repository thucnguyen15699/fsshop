package com.example.e_backend.service;

import com.example.e_backend.Model.Product;
import com.example.e_backend.Model.Review;
import com.example.e_backend.Model.User;
import com.example.e_backend.exception.ProductException;
import com.example.e_backend.repository.ProductRepository;
import com.example.e_backend.repository.ReviewRepository;
import com.example.e_backend.request.ReviewRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImplementation implements ReviewService{
    private ReviewRepository reviewRepository;
    private ProductService productService;
    private ProductRepository productRepository;

    public ReviewServiceImplementation(ReviewRepository reviewRepository,ProductService productService,ProductRepository productRepository) {
        this.reviewRepository=reviewRepository;
        this.productService=productService;
        this.reviewRepository=reviewRepository;
    }

    @Override
    public Review createReview(ReviewRequest req, User user) throws ProductException {
        Product product=productService.findProductById(req.getProductId());
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReview(req.getReview());
        review.setCreatedAt(LocalDateTime.now());


        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReview(Long productId) {

        return reviewRepository.getAllProductReview(productId);
    }
}
