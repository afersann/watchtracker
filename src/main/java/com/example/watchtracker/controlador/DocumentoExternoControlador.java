package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.DocumentoExterno;
import com.example.watchtracker.modelo.Reloj;
import com.example.watchtracker.servicio.DocumentoExternoServicio;
import com.example.watchtracker.servicio.RelojServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/documentos")
public class DocumentoExternoControlador {

    @Autowired
    private DocumentoExternoServicio documentoExternoServicio;

    @Autowired
    private RelojServicio relojServicio;

    @GetMapping
    public String mostrarDocumentos(Model model) {
        List<DocumentoExterno> documentos = documentoExternoServicio.listarDocumentos();
        List<Reloj> relojes = relojServicio.listarRelojes(); // Asegúrate que existe
        model.addAttribute("documentos", documentos);
        model.addAttribute("relojes", relojes);
        return "documentos";
    }

    @PostMapping
    public DocumentoExterno crearDocumento(@RequestBody DocumentoExterno documento) {
        return documentoExternoServicio.guardarDocumento(documento);
    }

    @PutMapping("/{id}")
    public DocumentoExterno actualizarDocumento(@PathVariable Long id, @RequestBody DocumentoExterno documentoActualizado) {
        documentoActualizado.setId(id);
        return documentoExternoServicio.guardarDocumento(documentoActualizado);
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarDocumento(@PathVariable Long id) {
        documentoExternoServicio.eliminarDocumento(id);
        return "redirect:/documentos";
    }

    @PostMapping("/subir")
    public DocumentoExterno subirDocumento(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("fuente") String fuente,
            @RequestParam("tipoDocumento") String tipoDocumento,
            @RequestParam("fechaObtencion") String fechaObtencion,
            @RequestParam("idReloj") Long idReloj
    ) {
        try {
            byte[] bytes = archivo.getBytes();
            Path ruta = Paths.get("uploads/" + archivo.getOriginalFilename());
            Files.write(ruta, bytes);

            Reloj reloj = relojServicio.buscarPorId(idReloj);

            DocumentoExterno documento = new DocumentoExterno();
            documento.setFuente(fuente);
            documento.setTipoDocumento(tipoDocumento);
            documento.setFechaObtencion(LocalDate.parse(fechaObtencion));
            documento.setUrlDocumento(ruta.toString());
            documento.setReloj(reloj);

            return documentoExternoServicio.guardarDocumento(documento);

        } catch (Exception e) {
            throw new RuntimeException("Error al subir el archivo: " + e.getMessage());
        }
    }

    @GetMapping("/descargar")
    public ResponseEntity<Resource> descargarArchivo(@RequestParam String nombre) {
        try {
            Path archivoPath = Paths.get("uploads").resolve(nombre).normalize();
            Resource recurso = new UrlResource(archivoPath.toUri());

            if (recurso.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                        .body(recurso);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/descargar/{id}")
    public ResponseEntity<Resource> descargarDocumento(@PathVariable Long id) {
        try {
            DocumentoExterno documento = documentoExternoServicio.buscarPorId(id);
            if (documento == null) {
                return ResponseEntity.notFound().build();
            }

            Path archivoPath = Paths.get(documento.getUrlDocumento()).normalize();
            Resource recurso = new UrlResource(archivoPath.toUri());

            if (recurso.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
                        .body(recurso);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
