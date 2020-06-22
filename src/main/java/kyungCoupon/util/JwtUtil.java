package kyungCoupon.util;

import javax.crypto.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import kyungCoupon.domain.User;

public class JwtUtil {

    SecretKey secretKey;


    public JwtUtil(String secretKey) {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String createToken(User user){
        JwtBuilder builder = Jwts.builder()
                .setSubject(user.getUserName())
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .claim("userName", user.getUserName());

        return builder.signWith(this.secretKey)
                .compact();

    }

    public Claims getClaims(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }
}
