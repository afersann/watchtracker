package com.example.watchtracker.servicio;

import com.example.watchtracker.modelo.Conversacion;
import com.example.watchtracker.modelo.Mensaje;
import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.ConversacionRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor          // ← inyección por constructor
public class ConversacionServicio {

    private final ConversacionRepositorio conversacionRepositorio;

    /*------------------------------------------------------------------
     * Conversaciones de un usuario
     *-----------------------------------------------------------------*/
    public List<Conversacion> obtenerConversacionesDeUsuario(Usuario usuario) {
        return conversacionRepositorio.findByUsuario1OrUsuario2(usuario, usuario);
    }

    /*------------------------------------------------------------------
     * Último mensaje de una conversación (ya cargada)
     *-----------------------------------------------------------------*/
    public Mensaje obtenerUltimoMensaje(Conversacion conversacion) {
        List<Mensaje> mensajes = conversacion.getMensajes();     // Ya viene cargado
        if (mensajes == null || mensajes.isEmpty()) return null;
        return mensajes.get(mensajes.size() - 1);
    }

    /*------------------------------------------------------------------
     * Buscar conversación exacta entre dos usuarios
     *-----------------------------------------------------------------*/
    public Optional<Conversacion> buscarConversacionEntre(Usuario u1, Usuario u2) {
        return conversacionRepositorio.findByUsuario1AndUsuario2OrUsuario2AndUsuario1(u1, u2, u1, u2);
    }

    /*------------------------------------------------------------------
     * Crear conversación si no existe
     *-----------------------------------------------------------------*/
    public Conversacion crearConversacionSiNoExiste(Usuario u1, Usuario u2) {
        return buscarConversacionEntre(u1, u2)
                .orElseGet(() -> {
                    Conversacion nueva = new Conversacion();
                    nueva.setUsuario1(u1);
                    nueva.setUsuario2(u2);
                    return conversacionRepositorio.save(nueva);
                });
    }

    /*------------------------------------------------------------------
     * Guardar (insertar / actualizar) conversación
     *-----------------------------------------------------------------*/
    public Conversacion guardarConversacion(Conversacion conversacion) {
        return conversacionRepositorio.save(conversacion);
    }

    /*------------------------------------------------------------------
     * Buscar conversación por id **con** mensajes cargados
     *-----------------------------------------------------------------*/
    public Optional<Conversacion> buscarPorId(Long id) {
        // Método con @EntityGraph en el repositorio
        return conversacionRepositorio.findWithMensajesById(id);
    }
}
