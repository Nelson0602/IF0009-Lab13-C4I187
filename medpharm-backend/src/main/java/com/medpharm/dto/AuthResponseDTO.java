package com.medpharm.dto;

// Respuesta del login con el token JWT y los datos básicos de la sesión
public class AuthResponseDTO {

    private String token;
    private String username;
    private String rol;

    public AuthResponseDTO(String token, String username, String rol) {
        this.token = token;
        this.username = username;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getRol() {
        return rol;
    }
}