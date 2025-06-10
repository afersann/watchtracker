// src/main/java/com/example/watchtracker/controlador/RecuperacionControlador.java
package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class RecuperacionControlador {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/recuperar")
    public String mostrarFormularioRecuperacion(Model model) {
        return "recuperar"; // este es tu HTML recuperar.html
    }
    @PostMapping("/recuperar")
    public String procesarRecuperacion(@RequestParam("usuarioOCorreo") String usuarioOCorreo) {
        Usuario usuario = usuarioRepositorio.findByNombreUsuario(usuarioOCorreo)
                .orElse(usuarioRepositorio.findByCorreo(usuarioOCorreo).orElse(null));

        if (usuario != null) {
            return "redirect:/cambiar-password?usuario=" + usuario.getNombreUsuario();
        } else {
            return "redirect:/recuperar?error=No se encontró el usuario";
        }
    }


    // GET: Muestra el formulario de cambio de password
    @GetMapping("/cambiar-password")
    public String mostrarFormularioCambio(@RequestParam("usuario") String nombreUsuario, Model model) {
        model.addAttribute("nombreUsuario", nombreUsuario);
        return "cambiar-password";
    }

    // POST: Procesa la nueva password
    @PostMapping("/cambiar-password")
    public String procesarCambioPassword(@RequestParam("nombreUsuario") String nombreUsuario,
                                           @RequestParam("nuevaContrasena") String nuevaContrasena,
                                           Model model) {

        Optional<Usuario> usuarioOpt = usuarioRepositorio.findByNombreUsuario(nombreUsuario);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setPasswordHash(passwordEncoder.encode(nuevaContrasena));
            usuarioRepositorio.save(usuario);
            return "redirect:/login?cambioExitoso";
        } else {
            model.addAttribute("error", "El usuario no fue encontrado.");
            return "cambiar-password";
        }
    }

}
