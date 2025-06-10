package com.example.watchtracker.servicio;

import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DetallesUsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String entrada) throws UsernameNotFoundException {
        // Buscar por correo o nombre de usuario
        Usuario usuario = usuarioRepositorio.findByCorreo(entrada)
                .or(() -> usuarioRepositorio.findByNombreUsuario(entrada))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo o nombre: " + entrada));

        // Convertimos el rol a mayúsculas para coincidir con "ROLE_ADMIN" que espera Spring
        List<SimpleGrantedAuthority> roles = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toUpperCase())
        );

        // Siempre usamos el correo como identificador interno
        return new User(usuario.getCorreo(), usuario.getPasswordHash(), roles);
    }
}
