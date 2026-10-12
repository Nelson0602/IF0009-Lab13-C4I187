package com.medpharm.service;

import com.medpharm.model.Medicamento;
import com.medpharm.repository.MedicamentoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

// Consulta del catálogo de medicamentos
@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    public List<Medicamento> findAll() {
        return medicamentoRepository.findAllByOrderByNombreAsc();
    }
}