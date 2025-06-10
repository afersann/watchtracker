package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.HistorialIdentificacion;
import com.example.watchtracker.modelo.HistorialTecnico;
import com.example.watchtracker.modelo.Reloj;
import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.HistorialIdentificacionRepositorio;
import com.example.watchtracker.repositorio.HistorialTecnicoRepositorio;
import com.example.watchtracker.repositorio.RelojRepositorio;
import com.example.watchtracker.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BusquedaControlador {

    private final RelojRepositorio relojRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final HistorialIdentificacionRepositorio historialIdentificacionRepositorio;
    private final HistorialTecnicoRepositorio historialTecnicoRepositorio;

    @GetMapping("/buscar")
    public String buscar(@RequestParam("query") String query, Model model) {
        // Buscar relojes (aunque la plantilla actual no los muestra, los dejamos por si se amplía luego)
        List<Reloj> relojes = relojRepositorio
                .findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCaseOrReferenciaContainingIgnoreCase(query, query, query);

        // Buscar usuarios (idem)
        List<Usuario> usuarios = usuarioRepositorio
                .findByNombreUsuarioContainingIgnoreCaseOrCorreoContainingIgnoreCase(query, query);

        // Resultados de identificaciones
        List<HistorialIdentificacion> resultadosIdentificacion = historialIdentificacionRepositorio
                .findByResultadoRoboflowContainingIgnoreCaseOrResultadoVisionContainingIgnoreCaseOrTextoDetectadoContainingIgnoreCase(query, query, query);

        // Resultados de historial técnico: **el atributo debe llamarse "resultadosTecnicos" para coincidir con Thymeleaf**
        List<HistorialTecnico> resultadosTecnicos = historialTecnicoRepositorio
                .findByTipoEventoContainingIgnoreCaseOrDescripcionEventoContainingIgnoreCase(query, query);

        model.addAttribute("query", query);
        model.addAttribute("relojes", relojes);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("resultadosIdentificacion", resultadosIdentificacion);
        model.addAttribute("resultadosTecnicos", resultadosTecnicos);



        return "resultados-busqueda";
    }
}
