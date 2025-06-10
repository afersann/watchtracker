package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    // GET: Listar todos los usuarios
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioServicio.listarUsuarios();
    }

    // POST: Crear nuevo usuario
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioServicio.guardarUsuario(usuario);
    }

    // PUT: Actualizar usuario existente
    @PutMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        usuarioActualizado.setId(id);
        return usuarioServicio.guardarUsuario(usuarioActualizado);
    }

    // DELETE: Eliminar usuario
    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioServicio.eliminarUsuario(id);
    }

    @GetMapping("/buscar")
    public List<Usuario> buscarUsuariosPorRol(@RequestParam String rol) {
        return usuarioServicio.buscarPorRol(rol);
    }




}
