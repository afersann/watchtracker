package com.example.watchtracker.controlador;

import com.example.watchtracker.modelo.Conversacion;
import com.example.watchtracker.modelo.Mensaje;
import com.example.watchtracker.modelo.Usuario;
import com.example.watchtracker.servicio.ConversacionServicio;
import com.example.watchtracker.servicio.MensajeServicio;
import com.example.watchtracker.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/inbox")
public class InboxControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ConversacionServicio conversacionServicio;

    @Autowired
    private MensajeServicio mensajeServicio;

    // ✅ Muestra todas las conversaciones del usuario autenticado
    @GetMapping
    public String verInbox(Model model, Principal principal) {
        Usuario usuarioActual = usuarioServicio.obtenerPorUsername(principal.getName());

        // ⚠️ Cambiado el nombre del método
        List<Conversacion> conversaciones = conversacionServicio.obtenerConversacionesDeUsuario(usuarioActual);
        Map<Long, Mensaje> ultimosMensajes = mensajeServicio.obtenerUltimosMensajes(conversaciones);

        model.addAttribute("usuarioActual", usuarioActual);
        model.addAttribute("conversaciones", conversaciones);
        model.addAttribute("ultimosMensajes", ultimosMensajes);
        return "inbox";
    }

    // ✅ Muestra una conversación específica, si el usuario tiene acceso
    @GetMapping("/{id}")
    public String verConversacion(@PathVariable Long id, Model model, Principal principal) {
        Usuario usuarioActual = usuarioServicio.obtenerPorUsername(principal.getName());

        Optional<Conversacion> optionalConversacion = conversacionServicio.buscarPorId(id);
        if (optionalConversacion.isEmpty()) {
            model.addAttribute("conversacion", null);
            return "conversacion";
        }

        Conversacion conversacion = optionalConversacion.get();

        // Validación: el usuario debe formar parte de la conversación
        if (!conversacion.getUsuario1().equals(usuarioActual) && !conversacion.getUsuario2().equals(usuarioActual)) {
            model.addAttribute("conversacion", null);
            return "conversacion";
        }

        List<Mensaje> mensajes = mensajeServicio.obtenerMensajesDeConversacion(conversacion);

        model.addAttribute("usuarioActual", usuarioActual);
        model.addAttribute("conversacion", conversacion);
        model.addAttribute("mensajes", mensajes);
        return "conversacion";
    }

    // ✅ Envía un nuevo mensaje dentro de una conversación
    @PostMapping("/{id}/enviar")
    public String enviarMensaje(@PathVariable Long id,
                                @RequestParam String contenido,
                                Principal principal) {
        Usuario usuarioActual = usuarioServicio.obtenerPorUsername(principal.getName());
        Optional<Conversacion> optionalConversacion = conversacionServicio.buscarPorId(id);

        if (optionalConversacion.isEmpty()) {
            return "redirect:/inbox";
        }

        Conversacion conversacion = optionalConversacion.get();

        // Seguridad: solo si participa en la conversación
        if (!conversacion.getUsuario1().equals(usuarioActual) && !conversacion.getUsuario2().equals(usuarioActual)) {
            return "redirect:/inbox";
        }

        mensajeServicio.enviarMensaje(conversacion, usuarioActual, contenido);

        return "redirect:/inbox/" + id;
    }

    // ✅ Iniciar o redirigir a una conversación con otro usuario
    @GetMapping("/iniciar/{id}")
    public String iniciarConversacion(@PathVariable Long id, Principal principal) {
        Usuario usuarioActual = usuarioServicio.obtenerPorUsername(principal.getName());
        Optional<Usuario> usuarioDestinoOpt = usuarioServicio.buscarUsuarioPorId(id);

        if (usuarioDestinoOpt.isEmpty() || usuarioDestinoOpt.get().equals(usuarioActual)) {
            return "redirect:/inbox";
        }

        Usuario usuarioDestino = usuarioDestinoOpt.get();

        // Crear o recuperar conversación
        Conversacion conversacion = conversacionServicio.crearConversacionSiNoExiste(usuarioActual, usuarioDestino);

        // Redirigir a la conversación
        return "redirect:/inbox/" + conversacion.getId();
    }


}
