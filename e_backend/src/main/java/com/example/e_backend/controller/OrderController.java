package com.example.e_backend.controller;

import com.example.e_backend.Model.Address;
import com.example.e_backend.Model.Order;
import com.example.e_backend.Model.User;
import com.example.e_backend.exception.OrderException;
import com.example.e_backend.service.OrderService;
import com.example.e_backend.service.UserException;
import com.example.e_backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;
    @PostMapping("/")
    public ResponseEntity<Order>createOrder(@RequestBody Address shippingAddress, @RequestHeader("Authorization") String jwt)throws UserException{
        User user=userService.findUserProfileByJwt(jwt);
        Order order=orderService.creatOder(user,shippingAddress);
//        Long productId = request.getProductId();
        System.out.println("order"+order);
        return new ResponseEntity<Order>(order, HttpStatus.CREATED);
    }
    @GetMapping("/user")
    public ResponseEntity<List<Order>>usersOrderHistory(
            @RequestHeader("Authorization") String jwt) throws UserException{
        User user =userService.findUserProfileByJwt(jwt);
        List<Order>orders=orderService.usersOrderHistory(user.getId());
         return new ResponseEntity<>(orders,HttpStatus.CREATED);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<Order>findOrderById(@PathVariable("Id") Long orderId,
                                              @RequestHeader("Authorization")String jwt)throws UserException, OrderException{
        User user=userService.findUserProfileByJwt(jwt);

        Order order= orderService.findOrderById(orderId);
        return new ResponseEntity<>(order,HttpStatus.ACCEPTED);
    }



}
