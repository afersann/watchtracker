package com.example.watchtracker.repositorio;

import com.example.watchtracker.modelo.HistorialTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialTecnicoRepositorio
        extends JpaRepository<HistorialTecnico, Long> {

     /* búsquedas existentes ------------------------------------------- */
     List<HistorialTecnico> findByTipoEventoIgnoreCase(String tipoEvento);
     List<HistorialTecnico> findByTipoEventoContainingIgnoreCaseOrDescripcionEventoContainingIgnoreCase(
             String tipo, String descripcion);
     List<HistorialTecnico> findByRelojId(Long id);
     List<HistorialTecnico> findByDescripcionEventoContainingIgnoreCase(String descripcionEvento);

     /* NUEVO ── valores distintos para los desplegables ---------------- */

     /** Tipos de evento distintos, en minúsculas y ordenados. */
     @Query("select distinct lower(h.tipoEvento) " +
             "from HistorialTecnico h " +
             "where h.tipoEvento is not null " +
             "order by lower(h.tipoEvento)")
     List<String> findTiposDistintos();

     /** Marcas distintas, en minúsculas y ordenadas. */
     @Query("select distinct lower(r.marca) " +
             "from HistorialTecnico h join h.reloj r " +
             "where r.marca is not null " +
             "order by lower(r.marca)")
     List<String> findMarcasDistintas();
}
