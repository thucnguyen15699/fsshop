package com.example.e_backend.controller;

import com.example.e_backend.Model.Cart;
import com.example.e_backend.Model.User;
import com.example.e_backend.exception.ProductException;
import com.example.e_backend.request.AddItemRequest;
import com.example.e_backend.response.ApiResponse;
import com.example.e_backend.service.CartService;
import com.example.e_backend.service.UserException;
import com.example.e_backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
//@Tag(name="Cart Mângement",description="find user cart,add item to cart")
public class CartControrller {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
//    @Operation(description = "find cart by user id")
    public ResponseEntity<Cart>findUserCart(@RequestHeader("Authorization") String jwt)throws UserException{
        User user= userService.findUserProfileByJwt(jwt);
        Cart cart=cartService.findUserCart(user.getId());

        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse>addItemToCart(@RequestBody AddItemRequest req,@RequestHeader("Authorization") String jwt)throws UserException, ProductException{
        User user=userService.findUserProfileByJwt(jwt);

        cartService.addCartItem(user.getId(),req);
        ApiResponse res=new ApiResponse();
        res.setStatus(true);
        res.setMessage("item added to cart");
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

}
