package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.Conversacion;
import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.ConversacionRepositorio;
import com.example.watchtracker.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
public class UsuarioAdminControlador {

    private final UsuarioRepositorio usuarioRepositorio;
    private final ConversacionRepositorio conversacionRepositorio;

    @GetMapping("/ver/{id}")
    public String verPerfilUsuario(@PathVariable Long id,
                                   Authentication authentication,
                                   Model model) {

        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Usuario actual = usuarioRepositorio.findByCorreo(authentication.getName())
                .or(() -> usuarioRepositorio.findByNombreUsuarioIgnoreCase(authentication.getName()))
                .orElseThrow();

        // ¿Ya existe conversación entre ambos?
        boolean conversacionExiste = conversacionRepositorio
                .existsByUsuario1AndUsuario2OrUsuario2AndUsuario1(actual, usuario, actual, usuario);

        model.addAttribute("usuarioPerfil", usuario);
        model.addAttribute("conversacionExiste", conversacionExiste);

        return "admin/ver-usuario";
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioRepositorio.findAll().stream()
                .filter(u -> !"Admin".equalsIgnoreCase(u.getRol()))
                .toList();

        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios"; // Asegúrate que la vista esté en templates/admin/usuarios.html
    }

    @PostMapping("/cambiar-rol")
    public String cambiarRol(@RequestParam Long id,
                             @RequestParam String nuevoRol,
                             RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getRol().equals(nuevoRol)) {
            usuario.setRol(nuevoRol);
            usuarioRepositorio.save(usuario);
        }

        return "redirect:/admin/usuarios";
    }




}
