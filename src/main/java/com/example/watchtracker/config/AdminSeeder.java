package com.example.watchtracker.config;

import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.UsuarioRepositorio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class AdminSeeder {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdminUser() {
        Optional<Usuario> existente = usuarioRepositorio.findByNombreUsuario("sudosu");
        if (existente.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombreUsuario("sudosu");
            admin.setCorreo("sudosu1@gmail.com");
            admin.setContrasena(passwordEncoder.encode("123"));
            admin.setRol("ADMIN");
            usuarioRepositorio.save(admin);
            System.out.println("✅ Usuario admin 'sudosu' creado correctamente.");
        } else {
            System.out.println("ℹ️ El usuario admin 'sudosu' ya existe.");
        }
    }
}
