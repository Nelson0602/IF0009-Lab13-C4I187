package com.medpharm.dto;

import jakarta.validation.constraints.NotBlank;

// Cuerpo del PATCH que cambia el estado de una receta
public class EstadoRequestDTO {

    @NotBlank(message = "El estado es requerido")
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}