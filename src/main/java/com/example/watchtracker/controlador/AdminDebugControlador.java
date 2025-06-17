package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/debug")
public class AdminDebugControlador {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }
	
@GetMapping("/cambiar-rol")
public String cambiarRol(@RequestParam String usuario, @RequestParam String nuevoRol) {
    return usuarioRepositorio.findByNombreUsuario(usuario).map(u -> {
        u.setRol(nuevoRol);
        usuarioRepositorio.save(u);
        return "Rol actualizado a " + nuevoRol;
    }).orElse("Usuario no encontrado");
}




}
