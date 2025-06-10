package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.HistorialTecnico;
import com.example.watchtracker.modelo.Reloj;
import com.example.watchtracker.servicio.HistorialTecnicoServicio;
import com.example.watchtracker.servicio.RelojServicio;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/historiales")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TECNICO')")
public class HistorialApiControlador {

    private final HistorialTecnicoServicio historialSrv;
    private final RelojServicio relojSrv;

    public HistorialApiControlador(HistorialTecnicoServicio historialSrv,
                                   RelojServicio relojSrv) {
        this.historialSrv = historialSrv;
        this.relojSrv = relojSrv;
    }

    /* ----------- LISTAR (JSON) ----------- */
    @GetMapping
    public List<HistorialTecnico> listar(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        return historialSrv.filtrarHistoriales(tipo, marca, desde, hasta);
    }

    /* ----------- CRUD (JSON) ----------- */
    @PostMapping
    public HistorialTecnico crear(@RequestBody HistorialTecnico h) {
        return historialSrv.guardarHistorial(h);
    }

    @PutMapping("/{id}")
    public HistorialTecnico actualizar(@PathVariable Long id,
                                       @RequestBody HistorialTecnico h) {
        h.setId(id);
        return historialSrv.guardarHistorial(h);
    }

    @DeleteMapping("/{id}")
    public void borrar(@PathVariable Long id) {
        historialSrv.eliminarHistorial(id);
    }

    /* ----------- Buscar por tipo (JSON) ----------- */
    @GetMapping("/buscar")
    public List<HistorialTecnico> buscarPorTipo(@RequestParam String tipo) {
        return historialSrv.buscarPorTipoEvento(tipo);
    }

    /* ----------- Exportar PDF ----------- */
    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportar(@RequestParam Long idReloj) {

        Reloj reloj = relojSrv.buscarPorId(idReloj);
        List<HistorialTecnico> hs = historialSrv.buscarPorRelojId(idReloj);
        hs.sort(Comparator.comparing(HistorialTecnico::getFechaEvento));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter w = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(w);
        try (Document doc = new Document(pdf)) {
            doc.add(new Paragraph("Historial Técnico – "
                    + reloj.getMarca() + " " + reloj.getModelo()
                    + " (ID: " + reloj.getId() + ")"));
            doc.add(new Paragraph(" "));
            for (HistorialTecnico h : hs) {
                doc.add(new Paragraph("Fecha: " + h.getFechaEvento()));
                doc.add(new Paragraph("Tipo: " + h.getTipoEvento()));
                doc.add(new Paragraph("Descripción: " + h.getDescripcionEvento()));
                doc.add(new Paragraph("Coste estimado: " + h.getCosteEstimado() + " €"));
                doc.add(new Paragraph("------------------------------"));
            }
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=historial_reloj_" + idReloj + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(baos.toByteArray());
    }
}
