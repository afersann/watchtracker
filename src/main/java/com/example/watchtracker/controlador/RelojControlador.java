package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.Reloj;
import com.example.watchtracker.servicio.RelojServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relojes")
public class RelojControlador {

    @Autowired
    private RelojServicio relojServicio;

    // GET: Listar todos los relojes
    @GetMapping
    public List<Reloj> listarRelojes() {
        return relojServicio.listarRelojes();
    }

    // POST: Crear nuevo reloj
    @PostMapping
    public Reloj crearReloj(@RequestBody Reloj reloj) {
        return relojServicio.guardarReloj(reloj);
    }
    // PUT: Actualizar reloj existente
    @PutMapping("/{id}")
    public Reloj actualizarReloj(@PathVariable Long id, @RequestBody Reloj relojActualizado) {
        relojActualizado.setId(id);
        return relojServicio.guardarReloj(relojActualizado);
    }

    // DELETE: Eliminar reloj
    @DeleteMapping("/{id}")
    public void eliminarReloj(@PathVariable Long id) {
        relojServicio.eliminarReloj(id);
    }
    @GetMapping("/buscar")
    public List<Reloj> buscarRelojes(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo) {

        if (marca != null) {
            return relojServicio.buscarPorMarca(marca);
        } else if (modelo != null) {
            return relojServicio.buscarPorModelo(modelo);
        } else {
            return relojServicio.listarRelojes();
        }
    }



}
