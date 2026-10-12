package com.medpharm.repository;

import com.medpharm.model.RecetaMedica;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// Acceso a datos de las recetas, las más recientes primero
public interface RecetaMedicaRepository extends JpaRepository<RecetaMedica, Long> {

    List<RecetaMedica> findAllByOrderByIdDesc();

    List<RecetaMedica> findByEstadoOrderByIdDesc(String estado);
}