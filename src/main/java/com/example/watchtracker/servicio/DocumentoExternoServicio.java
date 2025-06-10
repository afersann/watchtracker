package com.example.watchtracker.servicio;

import com.example.watchtracker.modelo.DocumentoExterno;
import com.example.watchtracker.modelo.Reloj;
import com.example.watchtracker.repositorio.DocumentoExternoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentoExternoServicio {

    @Autowired
    private DocumentoExternoRepositorio documentoExternoRepositorio;
    @Autowired
    private RelojServicio relojServicio;


    public List<DocumentoExterno> listarDocumentos() {
        return documentoExternoRepositorio.findAll();
    }

    public DocumentoExterno guardarDocumento(DocumentoExterno documento) {
        return documentoExternoRepositorio.save(documento);
    }

    public void eliminarDocumento(Long id) {
        documentoExternoRepositorio.deleteById(id);
    }

    public DocumentoExterno buscarPorId(Long id) {
        return documentoExternoRepositorio.findById(id).orElse(null);
    }
    public DocumentoExterno subirDocumento(MultipartFile archivo, String fuente, String tipoDocumento, String fechaObtencion, Long idReloj) {
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

            return guardarDocumento(documento);
        } catch (IOException e) {
            throw new RuntimeException("Error al subir el archivo", e);
        }
    }

}
