package com.example.watchtracker.controlador;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PanelControlador {

    @GetMapping("/panel")
    public String mostrarPanel(Model model, Authentication auth) {
        // Si en el futuro quieres añadir algo dinámico al panel, aquí puedes hacerlo
        return "panel"; // Esto carga tu panel.html
    }
}
