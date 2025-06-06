package com.tbd.backend.Config;

import com.tbd.backend.DTO.UsuarioDTO;
import com.tbd.backend.Entity.Usuario;
import io.jsonwebtoken.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class JwtMiddlewareService {

    private final SecretKey secretKey;

    public JwtMiddlewareService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(Usuario usuario) {
        Date expirationDate = new Date(System.currentTimeMillis() + 86400000); // 1 día

        return Jwts.builder()
                .claim("user_id", usuario.getId())
                .claim("name", usuario.getUsername())
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

    public UsuarioDTO decodeJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long id = claims.get("user_id", Long.class);
        String name = claims.get("name", String.class);

        UsuarioDTO usuariodto = new UsuarioDTO();
        usuariodto.setId(id.longValue());
        usuariodto.setUsername(name);

        return usuariodto;
    }
}