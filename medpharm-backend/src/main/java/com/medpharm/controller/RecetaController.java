package com.medpharm.controller;

import com.medpharm.dto.EstadoRequestDTO;
import com.medpharm.dto.RecetaRequestDTO;
import com.medpharm.dto.RecetaResponseDTO;
import com.medpharm.service.RecetaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Endpoints REST para consultar, registrar y actualizar recetas
@RestController
@RequestMapping("/api/v1/recetas")
public class RecetaController {

    private final RecetaService recetaService;

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    @GetMapping
    public List<RecetaResponseDTO> list() {
        return recetaService.findAll();
    }

    @GetMapping("/estado/{estado}")
    public List<RecetaResponseDTO> listByStatus(@PathVariable("estado") String status) {
        return recetaService.findByStatus(status);
    }

    // El médico que firma la receta se toma del token, no del cuerpo de la petición
    @PostMapping
    public ResponseEntity<RecetaResponseDTO> create(@Valid @RequestBody RecetaRequestDTO request,
            Authentication authentication) {
        RecetaResponseDTO created = recetaService.create(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}/estado")
    public RecetaResponseDTO changeStatus(@PathVariable("id") Long id,
            @Valid @RequestBody EstadoRequestDTO request) {
        return recetaService.updateStatus(id, request.getEstado());
    }
}