package com.example.watchtracker.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class HistorialIdentificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] imagen;
    private String resultadoRoboflow;
    private String resultadoVision;
    private String textoDetectado;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public String getTextoDetectado() {
        return textoDetectado;
    }

    public void setTextoDetectado(String textoDetectado) {
        this.textoDetectado = textoDetectado;
    }

}
