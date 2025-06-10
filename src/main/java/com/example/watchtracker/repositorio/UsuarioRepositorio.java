package com.example.watchtracker.repositorio;

import com.example.watchtracker.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {


    List<Usuario> findByRolIgnoreCase(String rol);

    Optional<Usuario> findByNombreUsuarioIgnoreCase(String nombreUsuario);

    List<Usuario> findByNombreUsuarioContainingIgnoreCase(String nombreUsuario);



    List<Usuario> findByNombreUsuarioContainingIgnoreCaseOrCorreoContainingIgnoreCase(String nombreUsuario, String correo);

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);




}
