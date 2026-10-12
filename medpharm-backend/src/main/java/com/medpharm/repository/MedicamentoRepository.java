package com.medpharm.repository;

import com.medpharm.model.Medicamento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// Acceso a datos del catálogo de medicamentos
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    List<Medicamento> findAllByOrderByNombreAsc();
}