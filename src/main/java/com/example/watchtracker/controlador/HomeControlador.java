package com.example.watchtracker.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeControlador {

    @GetMapping("/")
    public String landing() {
        return "landing";
    }
}
