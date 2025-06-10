package com.example.watchtracker.config;

import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.UsuarioRepositorio;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUsuarioAdvice {

    private final UsuarioRepositorio usuarioRepositorio;

    public GlobalUsuarioAdvice(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @ModelAttribute("usuarioActual")
    public Usuario getUsuarioAutenticado(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        return usuarioRepositorio.findByCorreo(authentication.getName())
                .or(() -> usuarioRepositorio.findByNombreUsuarioIgnoreCase(authentication.getName()))
                .orElse(null);
    }
}
