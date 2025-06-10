package com.example.watchtracker.repositorio;

import com.example.watchtracker.modelo.HistorialIdentificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialIdentificacionRepositorio extends JpaRepository<HistorialIdentificacion, Long> {

    // Buscar por coincidencias en el resultado de Roboflow
    List<HistorialIdentificacion> findByUsuarioOrderByFechaDesc(com.example.watchtracker.modelo.Usuario usuario);
    List<HistorialIdentificacion> findByResultadoRoboflowContainingIgnoreCase(String resultado);
    List<HistorialIdentificacion> findByResultadoRoboflowContainingIgnoreCaseOrResultadoVisionContainingIgnoreCaseOrTextoDetectadoContainingIgnoreCase(String robo, String vision, String texto);

    // También podrías añadir más filtros si lo necesitas en el futuro:
    // List<HistorialIdentificacion> findByResultadoVisionContainingIgnoreCase(String consulta);
    // List<HistorialIdentificacion> findByTextoDetectadoContainingIgnoreCase(String consulta);
}
