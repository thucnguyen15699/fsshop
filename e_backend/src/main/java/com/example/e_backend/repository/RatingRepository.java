package com.example.e_backend.repository;

import com.example.e_backend.Model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Long> {
    @Query("select r FROM Rating r where r.product.id=:productId")
    public List<Rating> getAllProductsRating(@Param("productId")Long productId);
}
