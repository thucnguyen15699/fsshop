package com.example.e_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtProvider {
    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    public String generateToken(Authentication auth){// Phương thức này tạo ra một token JWT dựa trên thông tin xác thực (Authentication) của người dùng.
        String jwt= Jwts.builder()
                .setIssuedAt(new Date()) //Đặt thời điểm tạo token là thời điểm hiện tại.
                .setExpiration(new Date(new Date().getTime()+84600000)) // Đặt thời điểm token hết hạn là thời điểm hiện tại cộng với 84.6 triệu mili giây (khoảng 24 giờ).
                .claim("email",auth.getName()) //Thêm một claim (thông tin bổ sung) vào token, trong trường hợp này là email của người dùng. auth.getName() trả về tên người dùng (email) từ đối tượng Authentication.
                .signWith(key).compact(); // Ký token sử dụng giá trị khóa bí mật key và sau đó chuyển đổi nó thành một chuỗi JWT đã ký.
        return jwt;
    }

    public String getEmailFromToken(String jwt){ //Phương thức này lấy ra email từ một token JWT.
        jwt=jwt.substring(7);// Loại bỏ phần tiêu đề "Bearer " từ chuỗi JWT, nếu nó tồn tại.

        //Sử dụng thư viện JWT để xác minh và giải mã token JWT. Nó sử dụng key để kiểm tra chữ ký trên JWT và sau đó lấy ra các claims (thông tin) bên trong token.
        Claims claims= Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

        //Lấy ra giá trị của claim "email" từ các claims trong token JWT. Đây là email của người dùng.
        String email= String.valueOf(claims.get("email"));
        return email;
    }
}
