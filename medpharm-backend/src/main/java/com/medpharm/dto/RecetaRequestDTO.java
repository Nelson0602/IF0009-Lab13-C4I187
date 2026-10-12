package com.medpharm.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

// Payload para registrar una receta nueva con todos sus medicamentos
public class RecetaRequestDTO {

    @NotBlank(message = "El nombre del paciente es requerido")
    @Size(min = 5, message = "El nombre del paciente debe tener al menos 5 caracteres")
    private String pacienteNombre;

    @NotEmpty(message = "La receta debe tener al menos un medicamento")
    @Valid
    private List<DetalleRequestDTO> detalles = new ArrayList<>();

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public List<DetalleRequestDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleRequestDTO> detalles) {
        this.detalles = detalles;
    }
}