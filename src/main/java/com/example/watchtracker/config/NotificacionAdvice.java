package com.example.watchtracker.config;

import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.servicio.MensajeServicio;
import com.example.watchtracker.servicio.UsuarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class NotificacionAdvice {

    private final UsuarioServicio usuarioServicio;
    private final MensajeServicio mensajeServicio;

    @ModelAttribute
    public void agregarMensajesNoLeidos(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            usuarioServicio.buscarPorUsernameOCorreo(authentication.getName())
                    .ifPresent(usuario -> {
                        long noLeidos = mensajeServicio.contarNoLeidosPorUsuario(usuario);
                        model.addAttribute("mensajesNoLeidos", noLeidos);
                    });
        }
    }
}
