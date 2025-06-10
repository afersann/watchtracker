package com.example.watchtracker.modelo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Data
public class Reloj {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private String referencia;
    private Integer añoFabricacion;
    private String descripcion;

    @Column(name = "foto_ruta")
    private String fotoRuta;

    @ManyToOne
    private Usuario usuario; // Usuario que registró este reloj
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "usuario_creacion_id")
    private Usuario usuarioCreacion;

    @ManyToOne
    @JoinColumn(name = "usuario_ultima_modificacion_id")
    private Usuario usuarioUltimaModificacion;

    private LocalDateTime fechaUltimaModificacion;

}
