package com.example.watchtracker.repositorio;

import com.example.watchtracker.modelo.Conversacion;
import com.example.watchtracker.modelo.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversacionRepositorio extends JpaRepository<Conversacion, Long> {

    /*------------------------------------------------------------------
     * 1. Todas las conversaciones de un usuario
     *-----------------------------------------------------------------*/
    List<Conversacion> findByUsuario1OrUsuario2(Usuario usuario1, Usuario usuario2);

    /*------------------------------------------------------------------
     * 2. Conversación exacta entre dos usuarios (cualquier orden)
     *-----------------------------------------------------------------*/
    Optional<Conversacion>
    findByUsuario1AndUsuario2OrUsuario2AndUsuario1(Usuario u1, Usuario u2,
                                                   Usuario u2b, Usuario u1b);

    /*------------------------------------------------------------------
     * 3. ¿Existe conversación entre dos usuarios?  (usado por UsuarioAdminControlador)
     *-----------------------------------------------------------------*/
    boolean
    existsByUsuario1AndUsuario2OrUsuario2AndUsuario1(Usuario u1, Usuario u2,
                                                     Usuario u2b, Usuario u1b);

    /*------------------------------------------------------------------
     * 4. Traer una conversación con sus mensajes (evita LazyInit Exception)
     *-----------------------------------------------------------------*/
    @EntityGraph(attributePaths = { "mensajes", "mensajes.emisor" })
    Optional<Conversacion> findWithMensajesById(Long id);
}
