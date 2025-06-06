package com.example.demo.config;

import com.example.demo.Entity.Cliente;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtMiddlewareService {

    private final SecretKey secretKey;

    public JwtMiddlewareService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(Cliente cliente) {
        Date expirationDate = new Date(System.currentTimeMillis() + 86400000); // 1 día

        return Jwts.builder()
                .claim("user_id", cliente.getId_cliente())
                .claim("name", cliente.getNombre_cliente())
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return !jws.getPayload().getExpiration().before(new Date());
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Cliente decodeJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long id = claims.get("user_id", Long.class);
        String name = claims.get("name", String.class);

        Cliente cliente = new Cliente();
        cliente.setId_cliente(id.intValue());
        cliente.setNombre_cliente(name);

        return cliente;
    }
}
