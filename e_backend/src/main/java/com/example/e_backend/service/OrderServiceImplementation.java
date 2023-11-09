package com.example.e_backend.service;

import com.example.e_backend.Model.*;
import com.example.e_backend.exception.OrderException;
import com.example.e_backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImplementation implements OrderService {

    private OrderRepository orderRepository;
    private AddrssRepository addrssRepository;
    private OrderItemService orderItemService;
    private OrderItemRepository orderItemRepository;
    private UserRepository userRepository;
    private CartRepository cartRepository;
    private CartService cartService;
    private ProductService productService;

    public OrderServiceImplementation(OrderRepository orderRepository,AddrssRepository addrssRepository,
                                      OrderItemRepository orderItemRepository,
                                      UserRepository userRepository,OrderItemService orderItemService,
                                      CartRepository cartRepository,CartService cartService)
    {
        this.orderRepository=orderRepository;
        this.addrssRepository=addrssRepository;
        this.orderItemService=orderItemService;
        this.userRepository=userRepository;
        this.cartRepository=cartRepository;
        this.cartService=cartService;
        this.orderItemRepository=orderItemRepository;

    }

    @Override
    public Order creatOder(User user, Address shippaddress) {
        shippaddress.setUser(user);
        Address address= addrssRepository.save(shippaddress);
        user.getAddress().add(address);
        userRepository.save(user);

        Cart cart= cartService.findUserCart(user.getId());
        List<OrderItem> orderItems= new ArrayList<>();

        for(CartItem item:cart.getCartItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSize(item.getSize());
            orderItem.setUserId(item.getUserId());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());

            OrderItem createdOrderItem = orderItemRepository.save(orderItem);

            orderItems.add(createdOrderItem);
        }

        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setOrderItems(orderItems);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setToatlDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscounte(cart.getDiscounte());
        createdOrder.setTotalItem(cart.getToatalItem());

        createdOrder.setShippingAddress(address);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus("Pending");
        createdOrder.getPaymentDetails().setStatus("Pending");
        createdOrder.setCreatedAt(LocalDateTime.now());

        Order saveOrder = orderRepository.save(createdOrder);

        for (OrderItem item:orderItems){
            item.setOrder(saveOrder);
            orderItemRepository.save(item);
        }
        return saveOrder;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        Optional<Order> otp = orderRepository.findById(orderId);
        if(otp.isPresent()){
            return otp.get();
        }
        throw new OrderException("order not exist with id"+orderId);
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        List<Order> orders= orderRepository.getUsersOrders(userId);

        return orders;
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("Placed");
        order.getPaymentDetails().setStatus("Completed");
        return order;
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        Order order=findOrderById(orderId);
        order.setOrderStatus("Confirm");

        return orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order=findOrderById(orderId);
        order.setOrderStatus("Shipped");
        return orderRepository.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("deliverdOrder");
        return orderRepository.save(order);
    }

    @Override
    public Order cancledOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("Canceled");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order=findOrderById(orderId);

        orderRepository.deleteById(orderId);
    }
}
