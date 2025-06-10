package com.example.watchtracker.controlador;

import com.example.watchtracker.servicio.RelojServicio;
import com.example.watchtracker.servicio.DocumentoExternoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DocumentoVistaControlador {

    @Autowired
    private DocumentoExternoServicio documentoExternoServicio;
    @Autowired
    private RelojServicio relojServicio;


    @GetMapping("/documentos/vista")
    public String listarDocumentos(Model model) {
        model.addAttribute("documentos", documentoExternoServicio.listarDocumentos());
        return "documentos"; // esta será la vista documentos.html
    }
    @GetMapping("/documentos/nuevo")
    public String mostrarFormularioDocumento(Model model) {
        model.addAttribute("relojes", relojServicio.listarRelojes());
        return "nuevo-documento";
    }

    @PostMapping("/documentos/guardar")
    public String procesarDocumento(@RequestParam("archivo") MultipartFile archivo,
                                    @RequestParam String fuente,
                                    @RequestParam String tipoDocumento,
                                    @RequestParam String fechaObtencion,
                                    @RequestParam Long idReloj) {
        documentoExternoServicio.subirDocumento(archivo, fuente, tipoDocumento, fechaObtencion, idReloj);
        return "redirect:/documentos/vista";
    }

}
