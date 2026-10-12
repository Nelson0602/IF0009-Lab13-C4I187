package com.medpharm.repository;

import com.medpharm.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// Acceso a datos de los usuarios del sistema
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
}