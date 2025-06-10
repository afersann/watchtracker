package com.example.watchtracker.repositorio;

import com.example.watchtracker.modelo.Mensaje;
import com.example.watchtracker.modelo.Conversacion;
import com.example.watchtracker.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MensajeRepositorio extends JpaRepository<Mensaje, Long> {

    List<Mensaje> findByConversacionOrderByFechaEnvioAsc(Conversacion conversacion);

    @Query("SELECT COUNT(m) FROM Mensaje m WHERE m.leido = false AND m.emisor <> :usuario AND " +
            "(m.conversacion.usuario1 = :usuario OR m.conversacion.usuario2 = :usuario)")
    long contarMensajesNoLeidosPorUsuario(@Param("usuario") Usuario usuario);

    List<Mensaje> findByConversacionAndLeidoFalseAndEmisorNot(Conversacion conversacion, Usuario usuario);
}
