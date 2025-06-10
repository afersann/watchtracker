package com.example.watchtracker.config;

import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.servicio.MensajeServicio;
import com.example.watchtracker.servicio.UsuarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
@RequiredArgsConstructor
public class DatosGlobalesAdvice {

    private final UsuarioServicio usuarioServicio;
    private final MensajeServicio mensajeServicio;

    @ModelAttribute
    public void cargarMensajesNoLeidos(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Usuario actual = usuarioServicio.buscarPorUsernameOCorreo(authentication.getName())
                    .orElse(null);

            if (actual != null) {
                long mensajesNoLeidos = mensajeServicio.contarNoLeidosPorUsuario(actual);
                model.addAttribute("mensajesNoLeidos", mensajesNoLeidos);
                System.out.println("Usuario: " + actual.getNombreUsuario() + ", No leídos: " + mensajesNoLeidos);

            }
        }
    }

}
