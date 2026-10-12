package com.medpharm.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Un renglón de medicamento dentro de la receta que se va a registrar
public class DetalleRequestDTO {

    @NotNull(message = "Debe seleccionar un medicamento")
    private Long medicamentoId;

    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotBlank(message = "La dosis indicada es requerida")
    private String dosisIndicada;

    public Long getMedicamentoId() {
        return medicamentoId;
    }

    public void setMedicamentoId(Long medicamentoId) {
        this.medicamentoId = medicamentoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDosisIndicada() {
        return dosisIndicada;
    }

    public void setDosisIndicada(String dosisIndicada) {
        this.dosisIndicada = dosisIndicada;
    }
}