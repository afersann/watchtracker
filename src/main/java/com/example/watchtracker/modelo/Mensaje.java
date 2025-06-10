package com.example.watchtracker.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario emisor;

    @ManyToOne
    private Conversacion conversacion;

    private String contenido;

    private LocalDateTime fechaEnvio = LocalDateTime.now();

    private boolean leido = false;
}
