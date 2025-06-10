package com.example.watchtracker.modelo;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "accesos")
@Data
public class Acceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "connected_at", nullable = false)
    private LocalDateTime connectedAt;

    @Override
    public String toString() {
        // Formato legible, p.ej. "2025-06-07 17:42"
        return connectedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setConnectedAt(LocalDateTime connectedAt) { this.connectedAt = connectedAt; }


}
