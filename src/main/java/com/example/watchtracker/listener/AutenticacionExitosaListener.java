package com.example.watchtracker.listener;

import com.example.watchtracker.modelo.Acceso;
import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.AccesoRepositorio;
import com.example.watchtracker.repositorio.UsuarioRepositorio;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AutenticacionExitosaListener
        implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UsuarioRepositorio usuarioRepo;
    private final AccesoRepositorio   accesoRepo;

    public AutenticacionExitosaListener(UsuarioRepositorio usuarioRepo,
                                        AccesoRepositorio accesoRepo) {
        this.usuarioRepo = usuarioRepo;
        this.accesoRepo  = accesoRepo;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String principal = event.getAuthentication().getName();
        Usuario usuario = usuarioRepo.findByCorreo(principal)
                .or(() -> usuarioRepo.findByNombreUsuarioIgnoreCase(principal))
                .orElse(null);
        if (usuario != null) {
            Acceso acceso = new Acceso();
            acceso.setUsuario(usuario);
            acceso.setConnectedAt(LocalDateTime.now());
            accesoRepo.save(acceso);
        }
    }
}
