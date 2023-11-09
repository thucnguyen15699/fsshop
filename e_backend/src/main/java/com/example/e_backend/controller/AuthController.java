package com.example.e_backend.controller;

import com.example.e_backend.Model.Cart;
import com.example.e_backend.Model.User;
import com.example.e_backend.config.JwtProvider;
import com.example.e_backend.repository.UserRepository;
import com.example.e_backend.request.LoginRequest;
import com.example.e_backend.service.CartService;
import com.example.e_backend.service.CustomeUserSeviceImplementation;
import com.example.e_backend.service.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserRepository userRepository;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private CustomeUserSeviceImplementation customeUserSeviceImplementation;
    private CartService cartService;


    public AuthController(UserRepository userRepository,
                          CustomeUserSeviceImplementation customeUserSeviceImplementation,
                          PasswordEncoder passwordEncoder,
                          JwtProvider jwtProvider,CartService cartService){
        this.userRepository=userRepository;
        this.customeUserSeviceImplementation=customeUserSeviceImplementation;
        this.passwordEncoder=passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.cartService=cartService;
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        String email=user.getEmail();
        String password=user.getPassword();
        String firstName= user.getFirstName();
        String lastName=user.getLastName();

        User isEmailExist = userRepository.findByEmail(email);

        if(isEmailExist!=null)
        {
            throw new UserException("Email is Already Used with Another Account");
        }



        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setFirstName(firstName);
        createdUser.setLastName(lastName);

        User saveUser = userRepository.save(createdUser);
        Cart cart= cartService.createCart(saveUser);

        Authentication authentication= new UsernamePasswordAuthenticationToken(saveUser.getEmail(),saveUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse(token,"Signup Success");
//        authResponse.setJwt(token);
//        authResponse.setMessage("Signup Success");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/sigin")
    public ResponseEntity<AuthResponse>loginUserHandler(@RequestBody LoginRequest loginRequest){
        String username = loginRequest.getEmail();
        String password =loginRequest.getPassword();

        Authentication authentication = authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse(token,"Signin Success");
//        authResponse.setJwt(token);
//        authResponse.setMessage("Signin Success");

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);

    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customeUserSeviceImplementation.loadUserByUsername(username);
        if (userDetails==null){
            throw new BadCredentialsException("invalid UserName");
        }
        if (!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
