package com.medpharm.dto;

import jakarta.validation.constraints.NotBlank;

// Datos que envía el cliente para iniciar sesión
public class LoginRequestDTO {

    @NotBlank(message = "El usuario es requerido")
    private String username;

    @NotBlank(message = "La contraseña es requerida")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}