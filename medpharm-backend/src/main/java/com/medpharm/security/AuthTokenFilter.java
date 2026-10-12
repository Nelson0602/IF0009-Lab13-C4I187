package com.medpharm.security;

import com.medpharm.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// Filtro que lee la cabecera Authorization: Bearer <token> y autentica la petición
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;

    public AuthTokenFilter(JwtUtils jwtUtils, UsuarioRepository usuarioRepository) {
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtUtils.validateToken(token)) {
                String username = jwtUtils.getUsernameFromToken(token);

                // Si el usuario del token existe, se registra en el contexto de seguridad
                usuarioRepository.findByUsername(username).ifPresent(user -> {
                    List<SimpleGrantedAuthority> authorities = List
                            .of(new SimpleGrantedAuthority("ROLE_" + user.getRol()));
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user.getUsername(), null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            }
        }

        filterChain.doFilter(request, response);
    }
}