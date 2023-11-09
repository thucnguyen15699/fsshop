package com.example.e_backend.controller;
import com.example.e_backend.Model.CartItem;
import com.example.e_backend.Model.User;
import com.example.e_backend.service.CartItemService;
import com.example.e_backend.exception.CartItemException;
import com.example.e_backend.response.ApiResponse;
import com.example.e_backend.service.UserException;
import com.example.e_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartItems")
public class CartItemController {
    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @DeleteMapping("/{cartItemId}")
    @Operation(description = "Remove Cart Item From Cart")
//    @io.swagger.v3.oas.annotations.responses.ApiResponse(description = "delete item")
    public ResponseEntity<ApiResponse>deleteCartItem(@PathVariable Long cartItemId,
                                                     @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(user.getId(),cartItemId);

        ApiResponse res= new ApiResponse();
        res.setMessage("item added t cart");
        res.setStatus(true);
        return new ResponseEntity<>(res,HttpStatus.OK);

    }

    @PutMapping("/{cartItemId}")
//    @Operation(description = "Update Item To Cart")
    public ResponseEntity<CartItem>updateCartItem(
            @RequestBody CartItem cartItem,
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt
    )throws UserException,CartItemException{
        User user = userService.findUserProfileByJwt(jwt);
        CartItem updatedCartItem= cartItemService.updateCartItem(user.getId(),cartItemId,cartItem);
        return new ResponseEntity<>(updatedCartItem,HttpStatus.OK);
    }


}
