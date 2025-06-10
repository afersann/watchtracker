package com.example.watchtracker.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class HistorialTecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaEvento;
    private String tipoEvento; // Revisión, reparación, mantenimiento...
    private String descripcionEvento;
    private Double costeEstimado;

    @ManyToOne
    private Reloj reloj; // El reloj al que pertenece este historial

    public void setId(Long id) {
        this.id = id;
    }
}
