package com.example.e_backend.controller;

import com.example.e_backend.Model.Product;
import com.example.e_backend.exception.ProductException;
import com.example.e_backend.request.CreateProductRequest;
import com.example.e_backend.response.ApiResponse;
import com.example.e_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public ResponseEntity<Product>createProduct(@RequestBody CreateProductRequest req){
        Product product=productService.createProduct(req);
//        System.out.println(product+"ádvc");
        return new ResponseEntity<Product>(product, HttpStatus.CREATED);
    }
    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse>deleteProduct(@PathVariable Long productId)throws ProductException {
        productService.deleteProduct(productId);
        ApiResponse res= new ApiResponse();
        res.setMessage("product deleted successfulls");
        res.setStatus(true);
        return new ResponseEntity<>(res,HttpStatus.OK);

    }
    @GetMapping("/all")
    public ResponseEntity<List<Product>>findAllProduct(){
        List<Product> products = productService.findAllProduct();
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProduct(@RequestBody Product req,@PathVariable Long productId)throws ProductException{
        Product product=productService.updateProduct(productId,req);
        return new ResponseEntity<Product>(product,HttpStatus.CREATED);
    }

    @PostMapping("/creates")
    public ResponseEntity<ApiResponse>createMultipleProduct(@RequestBody CreateProductRequest[] req){
        for (CreateProductRequest product:req){
            productService.createProduct(product);
        }
        ApiResponse res = new ApiResponse();
        res.setMessage("product create success");
        res.setStatus(true);

        return new ResponseEntity<>(res,HttpStatus.CREATED);
    }



}
