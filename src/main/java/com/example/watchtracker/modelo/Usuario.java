package com.example.watchtracker.modelo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreUsuario;
    private String correo;
    private String passwordHash;
    private String rol; // Admin, Técnico, Consultor
    private int mensajesHoy;
    private LocalDate fechaUltimoMensaje;
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();


    public void setId(Long id) {
        this.id = id;
    }
}
