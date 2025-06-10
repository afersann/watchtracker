package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AutenticacionControlador {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder codificador;

    @GetMapping("/registrarse")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registrarse";
    }



    @PostMapping("/registrarse")
    public String procesarRegistro(@ModelAttribute Usuario usuario, Model model) {
        if (usuario == null) {
            System.out.println("🚨 Usuario es null");
            model.addAttribute("error", "Error interno al procesar el formulario.");
            return "registrarse";
        }

        System.out.println("🟢 Datos recibidos: " + usuario.getNombreUsuario());

        if (usuarioRepositorio.findByNombreUsuario(usuario.getNombreUsuario()).isPresent()) {
            model.addAttribute("error", "El nombre de usuario ya existe.");
            return "registrarse";
        }

        usuario.setPasswordHash(codificador.encode(usuario.getPasswordHash()));
        usuario.setRol("Consultor");
        usuarioRepositorio.save(usuario);

        return "redirect:/login";
    }




}
