package com.medpharm.controller;

import com.medpharm.model.Medicamento;
import com.medpharm.service.MedicamentoService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Catálogo de medicamentos para llenar los selectores del frontend
@RestController
@RequestMapping("/api/v1/medicamentos")
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    public MedicamentoController(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }

    @GetMapping
    public List<Medicamento> list() {
        return medicamentoService.findAll();
    }
}