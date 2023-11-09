package com.example.e_backend.service;

import com.example.e_backend.Model.Product;
import com.example.e_backend.exception.ProductException;
import com.example.e_backend.request.CreateProductRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    public Product createProduct(CreateProductRequest req );

    public String deleteProduct(Long productld) throws ProductException;

    public Product updateProduct(Long productld, Product req) throws ProductException;
    public Product findProductById(Long id) throws ProductException;
    public List<Product> findProductByCategory(String category);

    public Page<Product>getAllProduct(String category,List<String>colors,List<String>sizes,Integer minPrice,Integer maxPrice,
    Integer minDiscount, String sort, String stock,Integer pageNumber,Integer pageSize);

    public List<Product> findAllProduct();
}
