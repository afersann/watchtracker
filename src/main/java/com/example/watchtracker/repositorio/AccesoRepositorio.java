package com.example.watchtracker.repositorio;

import com.example.watchtracker.modelo.Acceso;
import com.example.watchtracker.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccesoRepositorio extends JpaRepository<Acceso, Long> {


    List<Acceso> findTop10ByUsuarioOrderByConnectedAtDesc(Usuario usuario);

}
