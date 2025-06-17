package com.example.watchtracker.config;

import com.example.watchtracker.servicio.DetallesUsuarioServicio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filtroSeguridad(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Acceso público total (landing y recursos estáticos)
                .requestMatchers("/", "/login", "/registro", "/registrarse**", "/login?cambioExitoso**", "/cambiar-password**", "/recuperar").permitAll()
                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()

                // Acceso restringido por rol
                .requestMatchers("/panel", "/identificar-reloj").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/admin/usuarios/ver/**").permitAll()
                .requestMatchers("/relojes/**").hasAnyRole("ADMIN", "TECNICO", "CONSULTOR")
                .requestMatchers("/historiales/**", "/historial").hasAnyRole("ADMIN", "TECNICO", "CONSULTOR")
                .requestMatchers("/documentos/**").hasAnyRole("ADMIN", "TECNICO")
                .requestMatchers("/inbox/**").authenticated()
                .requestMatchers("/api/chat").permitAll()

                // Todo lo demás necesita autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/panel", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/access-denied");
                })
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder codificadorPassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager gestorAutenticacion(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
