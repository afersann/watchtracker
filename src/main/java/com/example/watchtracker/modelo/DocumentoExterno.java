package com.example.watchtracker.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class DocumentoExterno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fuente; // Por ejemplo: British Museum, Internet Archive...
    private String urlDocumento;
    private String tipoDocumento; // Imagen, Manual, Catálogo
    private LocalDate fechaObtencion;

    @ManyToOne
    private Reloj reloj; // El reloj asociado a este documento
    public void setId(Long id) {
        this.id = id;
    }


}
