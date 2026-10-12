package com.medpharm.dto;

import java.time.LocalDateTime;
import java.util.List;

// Receta completa que se devuelve al cliente, sin exponer las entidades
public class RecetaResponseDTO {

    private Long id;
    private String codigoReceta;
    private String pacienteNombre;
    private String medicoNombre;
    private String estado;
    private LocalDateTime fechaEmision;
    private List<DetalleResponseDTO> detalles;

    public RecetaResponseDTO(Long id, String codigoReceta, String pacienteNombre, String medicoNombre,
            String estado, LocalDateTime fechaEmision, List<DetalleResponseDTO> detalles) {
        this.id = id;
        this.codigoReceta = codigoReceta;
        this.pacienteNombre = pacienteNombre;
        this.medicoNombre = medicoNombre;
        this.estado = estado;
        this.fechaEmision = fechaEmision;
        this.detalles = detalles;
    }

    public Long getId() {
        return id;
    }

    public String getCodigoReceta() {
        return codigoReceta;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public String getMedicoNombre() {
        return medicoNombre;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public List<DetalleResponseDTO> getDetalles() {
        return detalles;
    }
}