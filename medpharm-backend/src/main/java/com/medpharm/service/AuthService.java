package com.medpharm.service;

import com.medpharm.dto.AuthResponseDTO;
import com.medpharm.dto.LoginRequestDTO;
import com.medpharm.model.Usuario;
import com.medpharm.repository.UsuarioRepository;
import com.medpharm.security.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Valida las credenciales y entrega el token JWT
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        // Se usa el mismo mensaje para usuario y contraseña incorrectos
        Usuario user = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }

        String token = jwtUtils.generateToken(user.getUsername());
        return new AuthResponseDTO(token, user.getUsername(), user.getRol());
    }
}