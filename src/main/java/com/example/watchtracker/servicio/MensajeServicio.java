package com.example.watchtracker.servicio;

import com.example.watchtracker.modelo.Mensaje;
import com.example.watchtracker.modelo.Conversacion;
import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.repositorio.MensajeRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MensajeServicio {

    @Autowired
    private MensajeRepositorio mensajeRepositorio;

    public List<Mensaje> obtenerMensajesDeConversacion(Conversacion conversacion) {
        return mensajeRepositorio.findByConversacionOrderByFechaEnvioAsc(conversacion);
    }

    public long contarNoLeidosPorUsuario(Usuario usuario) {
        return mensajeRepositorio.contarMensajesNoLeidosPorUsuario(usuario);
    }

    public Mensaje guardarMensaje(Mensaje mensaje) {
        return mensajeRepositorio.save(mensaje);
    }

    public List<Mensaje> obtenerNoLeidos(Conversacion conversacion, Usuario receptor) {
        return mensajeRepositorio.findByConversacionAndLeidoFalseAndEmisorNot(conversacion, receptor);
    }

    // ✅ NUEVO: Enviar mensaje
    public void enviarMensaje(Conversacion conversacion, Usuario emisor, String contenido) {
        Mensaje mensaje = new Mensaje();
        mensaje.setConversacion(conversacion);
        mensaje.setEmisor(emisor);
        mensaje.setContenido(contenido);
        mensaje.setLeido(false);
        mensaje.setFechaEnvio(LocalDateTime.now());

        mensajeRepositorio.save(mensaje);
    }

    // ✅ NUEVO: Obtener último mensaje por conversación
    public Map<Long, Mensaje> obtenerUltimosMensajes(List<Conversacion> conversaciones) {
        Map<Long, Mensaje> mapa = new HashMap<>();
        for (Conversacion conv : conversaciones) {
            List<Mensaje> mensajes = obtenerMensajesDeConversacion(conv);
            if (!mensajes.isEmpty()) {
                mapa.put(conv.getId(), mensajes.get(mensajes.size() - 1));
            }
        }
        return mapa;
    }
}
