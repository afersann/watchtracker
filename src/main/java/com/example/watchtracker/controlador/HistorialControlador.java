package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.HistorialIdentificacion;
import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.servicio.HistorialIdentificacionServicio;
import com.example.watchtracker.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HistorialControlador {

    @Autowired
    private HistorialIdentificacionServicio historialServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/historial")
    public String verHistorial(Model model) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioServicio.buscarPorCorreo(correo).orElse(null);

        if (usuario != null) {
            List<HistorialIdentificacion> historial = historialServicio.obtenerHistorialPorUsuario(usuario);

            // Codifica imágenes como Base64 y las agrega al modelo
            List<Map<String, Object>> registros = historial.stream()
                    .map(h -> {
                        Map<String, Object> datos = new HashMap<>();
                        datos.put("fecha", h.getFecha());
                        datos.put("resultadoRoboflow", h.getResultadoRoboflow());
                        datos.put("resultadoVision", h.getResultadoVision());
                        datos.put("imagenBase64", h.getImagen() != null ? Base64.getEncoder().encodeToString(h.getImagen()) : null);
                        datos.put("usuario", h.getUsuario()); // ← ✅ ESTA LÍNEA ES LA CLAVE
                        return datos;
                    })
                    .collect(Collectors.toList());


            model.addAttribute("historial", registros);
        }

        return "historial";
    }
}
