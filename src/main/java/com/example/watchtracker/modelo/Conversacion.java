package com.example.watchtracker.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Conversacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario1;

    @ManyToOne
    private Usuario usuario2;

    @OneToMany(mappedBy = "conversacion", cascade = CascadeType.ALL)
    private List<Mensaje> mensajes;

    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
