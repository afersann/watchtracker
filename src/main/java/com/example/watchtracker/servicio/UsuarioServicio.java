package com.example.watchtracker.servicio;

import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder codificadorPassword;

    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }
    public Usuario obtenerPorUsername(String nombreUsuario) {
        return usuarioRepositorio.findByNombreUsuarioIgnoreCase(nombreUsuario)
                .or(() -> usuarioRepositorio.findByCorreo(nombreUsuario))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + nombreUsuario));
    }



    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepositorio.findById(id);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepositorio.findByCorreo(correo);
    }

    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioRepositorio.findByNombreUsuario(nombreUsuario);
    }

    public Optional<Usuario> buscarPorUsernameOCorreo(String valor) {
        return usuarioRepositorio.findByCorreo(valor)
                .or(() -> usuarioRepositorio.findByNombreUsuarioIgnoreCase(valor));
    }

    public List<Usuario> buscarPorRol(String rol) {
        return usuarioRepositorio.findByRolIgnoreCase(rol);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        usuario.setPasswordHash(codificadorPassword.encode(usuario.getPasswordHash()));
        return usuarioRepositorio.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepositorio.deleteById(id);
    }


}
