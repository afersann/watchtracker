package com.example.watchtracker;

import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.UsuarioRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminCreator {

    @Bean
    public CommandLineRunner crearAdmin(UsuarioRepositorio repo, PasswordEncoder codificador) {
        return args -> {
            String correoAdmin = "admin@watchtracker.com";

            if (repo.findByCorreo(correoAdmin).isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombreUsuario("admin");
                admin.setCorreo(correoAdmin);
                admin.setPasswordHash(codificador.encode("admin123"));
                admin.setRol("Admin");

                repo.save(admin);
                System.out.println("✅ Usuario Admin creado con éxito.");
            } else {
                System.out.println("ℹ️ El usuario Admin ya existe.");
            }
        };
    }
}
