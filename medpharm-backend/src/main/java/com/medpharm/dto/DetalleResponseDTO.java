package com.medpharm.dto;

// Renglón de receta que se devuelve al cliente
public class DetalleResponseDTO {

    private String medicamentoNombre;
    private Integer cantidad;
    private String dosisIndicada;

    public DetalleResponseDTO(String medicamentoNombre, Integer cantidad, String dosisIndicada) {
        this.medicamentoNombre = medicamentoNombre;
        this.cantidad = cantidad;
        this.dosisIndicada = dosisIndicada;
    }

    public String getMedicamentoNombre() {
        return medicamentoNombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public String getDosisIndicada() {
        return dosisIndicada;
    }
}