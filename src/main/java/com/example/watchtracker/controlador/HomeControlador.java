package com.example.watchtracker.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeControlador {

    @GetMapping("/")
public String mostrarLanding() {
    return "landing";
}

}
