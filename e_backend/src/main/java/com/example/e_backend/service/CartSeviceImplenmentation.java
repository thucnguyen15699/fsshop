package com.example.e_backend.service;

import com.example.e_backend.Model.Cart;
import com.example.e_backend.Model.CartItem;
import com.example.e_backend.Model.Product;
import com.example.e_backend.Model.User;
import com.example.e_backend.exception.ProductException;
import com.example.e_backend.repository.CartRepository;
import com.example.e_backend.request.AddItemRequest;
import org.springframework.stereotype.Service;

@Service
public class CartSeviceImplenmentation implements CartService{
    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private ProductService productService;

    public CartSeviceImplenmentation(CartRepository cartRepository,CartItemService cartItemService,ProductService productService) {
        this.cartRepository=cartRepository;
        this.cartItemService=cartItemService;
        this.productService=productService;
    }

    @Override
    public Cart createCart(User user) {
        Cart cart=new Cart();
        cart.setUser(user);

        return cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException {
        Cart cart=cartRepository.findByUserId(userId);
        Product product=productService.findProductById(req.getProductId());

        CartItem isPresent= cartItemService.isCartItemExist(cart,product,req.getSize(),userId);
        if(isPresent==null){
            CartItem cartItem= new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);

            int price=req.getQuantity()*product.getDiscountedPrice();
            cartItem.setPrice(price);
            cartItem.setSize(req.getSize());

            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
        }
        return "Item Add To Cart";
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart =cartRepository.findByUserId(userId);
        int totalPrice=0;
        int totalDiscountedPrice=0;
        int totalItem=0;


        for(CartItem cartItem: cart.getCartItems()){
            totalPrice =totalPrice+cartItem.getPrice();
            totalDiscountedPrice= totalDiscountedPrice+cartItem.getDiscountedPrice();
            totalItem= totalItem+cartItem.getQuantity();
        }

        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setToatalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        cart.setDiscounte(totalPrice-totalDiscountedPrice);

        return cartRepository.save(cart);
    }
}
