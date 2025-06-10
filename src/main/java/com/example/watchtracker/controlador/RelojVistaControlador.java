package com.example.watchtracker.controlador;

import com.example.watchtracker.dto.GrupoRelojesPorReferencia;
import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.servicio.UsuarioServicio;
import com.example.watchtracker.modelo.Reloj;
import com.example.watchtracker.servicio.RelojServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class RelojVistaControlador {

    @Autowired
    private RelojServicio relojServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/relojes/ver/{id}")
    public String verReloj(@PathVariable Long id, Model model) {
        Reloj reloj = relojServicio.buscarPorId(id);
        model.addAttribute("reloj", reloj);

        List<Long> ids = relojServicio.listarRelojes().stream()
                .map(Reloj::getId)
                .sorted()
                .toList();

        Long siguienteId = null;
        Long primeroId = ids.isEmpty() ? null : ids.get(0);

        for (int i = 0; i < ids.size(); i++) {
            if (Objects.equals(ids.get(i), id) && i + 1 < ids.size()) {
                siguienteId = ids.get(i + 1);
                break;
            }
        }

        if (siguienteId == null && primeroId != null) {
            siguienteId = primeroId; // volver al primero si es el último
        }

        model.addAttribute("siguienteId", siguienteId);

        List<Map<String, String>> breadcrumbs = new ArrayList<>();
        breadcrumbs.add(Map.of("label", "Panel", "url", "/panel"));
        breadcrumbs.add(Map.of("label", "Relojes", "url", "/relojes"));
        breadcrumbs.add(Map.of("label", "Detalle", "url", "")); // actual

        model.addAttribute("breadcrumbs", breadcrumbs);

        return "reloj/ver";
    }



    // Mostrar tabla de relojes
    @GetMapping("/relojes")
    public String mostrarRelojesAgrupados(Model model) {
        List<Reloj> todos = relojServicio.listarRelojes().stream()
                .sorted(Comparator.comparing(Reloj::getId)) // Ordenar por ID antes de agrupar
                .toList();

        Map<String, List<Reloj>> agrupados = todos.stream()
                .filter(r -> r.getReferencia() != null)
                .collect(Collectors.groupingBy(
                        Reloj::getReferencia,
                        LinkedHashMap::new, // para mantener el orden de inserción
                        Collectors.toList()
                ));

        // Ordenar los relojes dentro de cada grupo
        agrupados.replaceAll((ref, lista) ->
                lista.stream().sorted(Comparator.comparing(Reloj::getId)).toList()
        );

        List<GrupoRelojesPorReferencia> grupos = agrupados.entrySet().stream()
                .filter(e -> e.getKey() != null && e.getValue() != null && !e.getValue().isEmpty())
                .map(e -> new GrupoRelojesPorReferencia(e.getKey(), e.getValue()))
                .collect(Collectors.toList());


        model.addAttribute("gruposPorReferencia", grupos);
        return "relojes";
    }


    // Mostrar formulario nuevo
    @GetMapping("/relojes/nuevo")
    public String mostrarFormularioNuevoReloj(Model model) {
        model.addAttribute("reloj", new Reloj());
        return "nuevo-reloj";
    }

    @GetMapping("/relojes/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Reloj reloj = relojServicio.buscarPorId(id);
        model.addAttribute("reloj", reloj);
        model.addAttribute("usuarios", usuarioServicio.listarUsuarios()); // para seleccionar usuario
        return "nuevo-reloj"; // usamos misma vista que crear
    }
    @GetMapping("/relojes/eliminar/{id}")
    public String eliminarReloj(@PathVariable Long id) {
        relojServicio.eliminarReloj(id);
        return "redirect:/relojes";
    }



    // Guardar reloj desde formulario
    @PostMapping("/relojes/guardar")
    public String guardarReloj(@ModelAttribute Reloj reloj,
                               @RequestParam("imagen") MultipartFile imagen) throws IOException {

        // Obtener el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario = auth.getName();
        System.out.println(nombreUsuario);

        Usuario usuario = usuarioServicio.buscarPorCorreo(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + nombreUsuario));

        // Subida de imagen (si hay)
        if (imagen != null && !imagen.isEmpty()) {
            String nombreArchivo = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
            Path rutaArchivo = Paths.get("src/main/resources/static/img/relojes", nombreArchivo);
            Files.createDirectories(rutaArchivo.getParent());
            Files.write(rutaArchivo, imagen.getBytes());
            reloj.setFotoRuta("/img/relojes/" + nombreArchivo); // ruta relativa accesible por el navegador
        }

        // Lógica de creación o edición
        if (reloj.getId() == null) {
            // Nuevo reloj
            reloj.setUsuarioCreacion(usuario);
        } else {
            // Edición: mantener el creador original
            Reloj original = relojServicio.buscarPorId(reloj.getId());
            reloj.setUsuarioCreacion(original.getUsuarioCreacion());

            // Mantener imagen anterior si no se subió una nueva
            if ((imagen == null || imagen.isEmpty()) && original.getFotoRuta() != null) {
                reloj.setFotoRuta(original.getFotoRuta());
            }
        }

        // Siempre actualizar modificador y fecha
// Siempre actualizar modificador y fecha
        reloj.setUsuarioUltimaModificacion(usuario);
        reloj.setFechaUltimaModificacion(LocalDateTime.now());

// Si no se subió imagen y no hay una anterior, usar imagen por defecto
        if (reloj.getFotoRuta() == null || reloj.getFotoRuta().isBlank()) {
            reloj.setFotoRuta("/img/relojes/default.png");
        }

        relojServicio.guardarReloj(reloj);
        return "redirect:/relojes";

    }



}
