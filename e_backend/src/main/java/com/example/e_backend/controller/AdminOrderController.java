package com.example.e_backend.controller;

import com.example.e_backend.Model.Order;
import com.example.e_backend.exception.OrderException;
import com.example.e_backend.response.ApiResponse;
import com.example.e_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {
//    @Qualifier("")
    @Autowired
//    @Qualifier("OrderService")
    private OrderService orderService;



    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrdersHandler(){
        List<Order> orders=orderService.getAllOrders();
        return new ResponseEntity<List<Order>>(orders, HttpStatus.ACCEPTED);
    }
    @PutMapping("/{orderId}/confirmed")
     public ResponseEntity<Order> ConfirmedOrderHandler(@PathVariable long orderId, @RequestHeader("Authorization") String jwt)throws OrderException{
        Order order =orderService.confirmedOrder(orderId);
        return new ResponseEntity<>(order,HttpStatus.OK);
     }
    @PutMapping("/{orderId}/ship")
     public ResponseEntity<Order>ShippedOrderHandler(@PathVariable Long orderId,@RequestHeader("Authorization") String jwt)throws OrderException{
         Order order=orderService.shippedOrder(orderId);
         return new ResponseEntity<>(order,HttpStatus.OK);
    }
    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Order>DeliveredOrderHandler(@PathVariable Long orderId,@RequestHeader("Authorization") String jwt) throws  OrderException{
        Order order= orderService.deliveredOrder(orderId);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order>CancelOrderHandler(@PathVariable Long orderId,@RequestHeader("Authorization") String jwt) throws  OrderException{
        Order order= orderService.cancledOrder(orderId);
        return new ResponseEntity<>(order,HttpStatus.OK);
    }
    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException {
        orderService.deleteOrder(orderId);

        ApiResponse res = new ApiResponse();
        res.setMessage("order deleted successfully");
        res.setStatus(true);

        return new ResponseEntity<>(res,HttpStatus.OK);
    }




















}

