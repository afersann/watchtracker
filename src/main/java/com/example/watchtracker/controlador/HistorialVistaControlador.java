package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.HistorialTecnico;
import com.example.watchtracker.modelo.Reloj;
import com.example.watchtracker.servicio.HistorialTecnicoServicio;
import com.example.watchtracker.servicio.RelojServicio;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Controlador de VISTAS (Thymeleaf) para Historial Técnico.
 *   - GET /historiales           (listado con filtros)
 *   - GET /historiales/nuevo     (formulario nuevo)
 *   - GET /historiales/nuevo/{idReloj}
 *   - POST /historiales/guardar
 *   - GET /historiales/editar/{id}
 *   - GET /historiales/eliminar/{id}
 *   - GET /historiales/reloj/{idReloj}
 */
@Controller
@RequestMapping("/historiales")
@PreAuthorize("hasAnyRole('ROLE_TECNICO','ROLE_ADMIN','ROLE_CONSULTOR')")
public class HistorialVistaControlador {

    private final HistorialTecnicoServicio historialSrv;
    private final RelojServicio relojSrv;

    public HistorialVistaControlador(HistorialTecnicoServicio historialSrv,
                                     RelojServicio relojSrv) {
        this.historialSrv = historialSrv;
        this.relojSrv = relojSrv;
    }

    /* ---------- LISTADO con filtros ---------- */
    /* ---------- LISTADO con filtros y listas para los selects ---------- */
    @GetMapping
    public String verHistoriales(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            Model model) {

        model.addAttribute("historiales",
                historialSrv.filtrarHistoriales(tipo, marca, desde, hasta));

        /* ► listas para los desplegables */
        model.addAttribute("tipos",  historialSrv.obtenerTiposDistintos());
        model.addAttribute("marcas", historialSrv.obtenerMarcasDistintas());

        /* breadcrumbs opcionales */
        model.addAttribute("breadcrumbs", List.of(
                Map.of("label", "Panel", "url", "/panel"),
                Map.of("label", "Historial Técnico", "url", "")
        ));
        return "historiales";
    }


    /* ---------- NUEVO ---------- */
    @GetMapping("/nuevo")
    public String nuevoHistorial(Model model) {
        model.addAttribute("historial", new HistorialTecnico());
        model.addAttribute("relojes", relojSrv.listarRelojes());
        return "nuevo-historial";
    }

    @GetMapping("/nuevo/{idReloj}")
    public String nuevoHistorialDesdeReloj(@PathVariable Long idReloj, Model model) {
        Reloj reloj = relojSrv.buscarPorId(idReloj);
        if (reloj == null) return "redirect:/historiales";

        HistorialTecnico h = new HistorialTecnico();
        h.setReloj(reloj);

        model.addAttribute("historial", h);
        model.addAttribute("relojes", List.of(reloj));
        return "nuevo-historial";
    }

    /* ---------- GUARDAR ---------- */
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute HistorialTecnico h) {
        // ⚠ Recupera el reloj completo desde la base de datos usando el ID
        if (h.getReloj() != null && h.getReloj().getId() != null) {
            Reloj relojCompleto = relojSrv.buscarPorId(h.getReloj().getId());
            h.setReloj(relojCompleto); // ← aquí se asigna el reloj ya persistido
        }

        historialSrv.guardarHistorial(h);
        return "redirect:/historiales";
    }


    /* ---------- EDITAR ---------- */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        HistorialTecnico h = historialSrv.buscarPorId(id);
        if (h == null) return "redirect:/historiales";

        model.addAttribute("historial", h);
        model.addAttribute("relojes", relojSrv.listarRelojes());
        return "nuevo-historial";
    }

    /* ---------- ELIMINAR ---------- */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        historialSrv.eliminarHistorial(id);
        return "redirect:/historiales";
    }

    /* ---------- HISTORIAL DE UN RELOJ ---------- */
    @GetMapping("/reloj/{idReloj}")
    public String historialPorReloj(@PathVariable Long idReloj, Model model) {
        Reloj reloj = relojSrv.buscarPorId(idReloj);
        if (reloj == null) return "redirect:/historiales";

        model.addAttribute("reloj", reloj);
        model.addAttribute("historiales", historialSrv.buscarPorRelojId(idReloj));
        return "historiales-reloj";
    }
}
