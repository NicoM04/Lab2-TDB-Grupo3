package com.example.demo.config;

import com.example.demo.Entity.Cliente;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtMiddlewareService jwtMiddlewareService;

    public JwtAuthenticationFilter(JwtMiddlewareService jwtMiddlewareService) {
        this.jwtMiddlewareService = jwtMiddlewareService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtMiddlewareService.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado.");
            return;
        }

        Cliente cliente = jwtMiddlewareService.decodeJWT(token);

        // Se establece la autenticación en el contexto de seguridad
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                cliente, null, Collections.emptyList());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
