package com.thuc.controller;

import com.thuc.Model.User;
import com.thuc.config.JwtProvider;
import com.thuc.repository.UserRepository;
import com.thuc.request.LoginRequest;
import com.thuc.service.CustomeUserSeviceImplementation;
import com.thuc.service.UserException;
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
    public AuthController(UserRepository userRepository,
                          CustomeUserSeviceImplementation customeUserSeviceImplementation,
                          PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.customeUserSeviceImplementation=customeUserSeviceImplementation;
        this.passwordEncoder=passwordEncoder;
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse>createUserHandler(@RequestBody User user) throws UserException{
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

        Authentication authentication= new UsernamePasswordAuthenticationToken(saveUser.getEmail(),saveUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse(token,"Signup Success");

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
