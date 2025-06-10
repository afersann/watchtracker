package com.example.watchtracker.repositorio;

import com.example.watchtracker.modelo.DocumentoExterno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoExternoRepositorio extends JpaRepository<DocumentoExterno, Long> {
    // Consultas personalizadas opcionales en el futuro
}
