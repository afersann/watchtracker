package com.example.watchtracker.servicio;

import com.example.watchtracker.modelo.HistorialTecnico;
import com.example.watchtracker.repositorio.HistorialTecnicoRepositorio;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistorialTecnicoServicio {

    private final HistorialTecnicoRepositorio repo;

    public HistorialTecnicoServicio(HistorialTecnicoRepositorio repo) {
        this.repo = repo;
    }

    /* ---------- CRUD & búsquedas simples ---------------------------- */
    public List<HistorialTecnico> listarHistoriales()          { return repo.findAll(); }
    public Optional<HistorialTecnico> buscarHistorialPorId(Long id) { return repo.findById(id); }
    public HistorialTecnico guardarHistorial(HistorialTecnico h) {
        if (h.getReloj() == null || h.getReloj().getId() == null) {
            throw new IllegalArgumentException("El evento debe estar vinculado a un reloj válido.");
        }

        return repo.save(h);
    }

    public void eliminarHistorial(Long id)                         { repo.deleteById(id); }
    public List<HistorialTecnico> buscarPorTipoEvento(String t)    { return repo.findByTipoEventoIgnoreCase(t); }
    public HistorialTecnico buscarPorId(Long id)                   { return repo.findById(id).orElse(null); }
    public List<HistorialTecnico> buscarPorRelojId(Long id)        { return repo.findByRelojId(id); }

    /* ---------- NUEVO: listas para los <select> --------------------- */
    public List<String> obtenerTiposDistintos()  { return repo.findTiposDistintos();  }
    public List<String> obtenerMarcasDistintas() { return repo.findMarcasDistintas(); }

    /* ---------- Filtro múltiple (con variables finales) ------------- */
    public List<HistorialTecnico> filtrarHistoriales(String tipo,
                                                     String marca,
                                                     LocalDate desde,
                                                     LocalDate hasta) {

        final String t = (tipo  == null || tipo.isBlank())  ? null : tipo.trim();
        final String m = (marca == null || marca.isBlank()) ? null : marca.trim();

        return repo.findAll().stream()
                .filter(h -> t == null ||
                        (h.getTipoEvento() != null &&
                                h.getTipoEvento().equalsIgnoreCase(t)))
                .filter(h -> m == null ||
                        (h.getReloj() != null &&
                                h.getReloj().getMarca() != null &&
                                h.getReloj().getMarca().equalsIgnoreCase(m)))
                .filter(h -> desde == null ||
                        (h.getFechaEvento() != null &&
                                !h.getFechaEvento().isBefore(desde)))
                .filter(h -> hasta == null ||
                        (h.getFechaEvento() != null &&
                                !h.getFechaEvento().isAfter(hasta)))
                .collect(Collectors.toList());
    }
}
