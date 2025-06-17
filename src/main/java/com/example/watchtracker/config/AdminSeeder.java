package com.example.watchtracker.config;

import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.UsuarioRepositorio;
import com.example.watchtracker.enums.Rol; // si usas enum
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String correo = "sudosu1@gmail.com";
        if (!usuarioRepositorio.existsByCorreo(correo)) {
            Usuario admin = new Usuario();
            admin.setNombre("sudosu");
            admin.setCorreo(correo);
            admin.setPassword(passwordEncoder.encode("123"));
            admin.setRol(Rol.ADMIN); // o admin.setRol("ADMIN"); si es String
            admin.setFechaRegistro(LocalDate.now());
            usuarioRepositorio.save(admin);
            System.out.println("✔ Usuario admin creado correctamente");
        } else {
            System.out.println("ℹ Usuario admin ya existe");
        }
    }
}
