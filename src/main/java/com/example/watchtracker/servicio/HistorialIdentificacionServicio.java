package com.example.watchtracker.servicio;

import com.example.watchtracker.modelo.HistorialIdentificacion;
import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.HistorialIdentificacionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistorialIdentificacionServicio {

    @Autowired
    private HistorialIdentificacionRepositorio repositorio;

    public List<HistorialIdentificacion> obtenerHistorialPorUsuario(Usuario usuario) {
        return repositorio.findByUsuarioOrderByFechaDesc(usuario);
    }


    public void guardarHistorial(byte[] imagen, String roboflow, String vision, Usuario usuario)
 {
        HistorialIdentificacion historial = new HistorialIdentificacion();
        historial.setImagen(imagen);
        historial.setResultadoRoboflow(roboflow);
        historial.setResultadoVision(vision);
        historial.setFecha(LocalDateTime.now());
        historial.setUsuario(usuario);

        repositorio.save(historial);
    }

}
