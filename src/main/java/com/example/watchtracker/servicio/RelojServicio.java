package com.example.watchtracker.servicio;

import com.example.watchtracker.modelo.Reloj;
import com.example.watchtracker.repositorio.RelojRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RelojServicio {

    @Autowired
    private RelojRepositorio relojRepositorio;

    public List<Reloj> listarRelojes() {
        return relojRepositorio.findAll();
    }

    public List<Reloj> obtenerTodosLosRelojes() {
        return relojRepositorio.findAll();
    }

    public Optional<Reloj> buscarRelojPorId(Long id) {
        return relojRepositorio.findById(id);
    }

    public Reloj guardarReloj(Reloj reloj) {
        return relojRepositorio.save(reloj);
    }

    public void eliminarReloj(Long id) {
        relojRepositorio.deleteById(id);
    }
    public List<Reloj> buscarPorMarca(String marca) {
        return relojRepositorio.findByMarcaContainingIgnoreCase(marca);
    }

    public List<Reloj> buscarPorModelo(String modelo) {
        return relojRepositorio.findByModeloContainingIgnoreCase(modelo);
    }
    public Reloj buscarPorId(Long id) {
        return relojRepositorio.findById(id).orElseThrow(() ->
                new RuntimeException("Reloj no encontrado con ID: " + id));
    }


}
