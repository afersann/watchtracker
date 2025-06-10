package com.example.watchtracker.repositorio;

import com.example.watchtracker.modelo.Reloj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelojRepositorio extends JpaRepository<Reloj, Long> {
    List<Reloj> findByMarcaContainingIgnoreCase(String marca);

    List<Reloj> findByModeloContainingIgnoreCase(String modelo);

    List<Reloj> findByReferenciaContainingIgnoreCase(String referencia);

    List<Reloj> findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCaseOrReferenciaContainingIgnoreCase(String marca, String modelo, String referencia);
}
