package com.example.watchtracker.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SeguridadControlador {

    @GetMapping("/access-denied")
    public String accesoDenegado() {
        return "access-denied";
    }

    // (Aquí podrías poner también otros mapeos genéricos, p. ej. /login, /panel, etc.)
}
