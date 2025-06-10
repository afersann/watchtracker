package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.Acceso;
import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.AccesoRepositorio;
import com.example.watchtracker.repositorio.UsuarioRepositorio;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/usuario")
public class AccesoControlador {

    private final AccesoRepositorio accesoRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;

    public AccesoControlador(AccesoRepositorio accesoRepositorio,
                             UsuarioRepositorio usuarioRepositorio) {
        this.accesoRepositorio  = accesoRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @GetMapping("/accesos")
    public String verHistorialAccesos(Model model, Authentication authentication) {
        Usuario usuario = usuarioRepositorio
                .findByCorreo(authentication.getName())
                .or(() -> usuarioRepositorio.findByNombreUsuarioIgnoreCase(authentication.getName()))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + authentication.getName()));

        List<Acceso> accesos = accesoRepositorio
                .findTop10ByUsuarioOrderByConnectedAtDesc(usuario);

        model.addAttribute("usuario", usuario);
        model.addAttribute("accesos", accesos);
        return "usuario/accesos";
    }
}
