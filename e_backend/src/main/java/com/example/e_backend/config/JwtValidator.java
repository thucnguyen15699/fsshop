package com.example.e_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt= request.getHeader(JwtConstant.JWT_HEADER);
        if (jwt!=null){
            //bearere
            jwt=jwt.substring(7); //Dòng này cắt bỏ phần "Bearer " (hoặc bất kỳ phần nào trước JWT) để lấy ra chuỗi JWT thô.
            try{
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());//chua key
                //Dòng này sử dụng thư viện JWT để kiểm tra và xác minh JWT. Nó sử dụng key để kiểm tra chữ ký trên JWT và sau đó lấy ra các thông tin bên trong JWT (claims) như email và danh sách các quyền truy cập.
                Claims claims= Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                String email= String.valueOf(claims.get("email"));// lay eamal

                String authorities = String.valueOf(claims.get("authorities")); // lay quyen
                // Chuyển đổi danh sách quyền truy cập từ chuỗi thành danh sách đối tượng GrantedAuthority.
                List<GrantedAuthority> auths= AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                //Tạo một đối tượng Authentication với thông tin người dùng (email) và danh sách quyền truy cập (auths) đã được lấy từ JWT.
                Authentication authentication= new UsernamePasswordAuthenticationToken(email,null,auths);
                //Đặt đối tượng Authentication vào SecurityContext để xác minh rằng người dùng đã được xác thực.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (Exception e ){
                throw new BadCredentialsException("invalid token... from jwt validator");
            }


        }
        filterChain.doFilter(request,response);
    }
}