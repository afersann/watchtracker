package com.example.watchtracker.config;

import com.example.watchtracker.servicio.DetallesUsuarioServicio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filtroSeguridad(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/panel", "/identificar-reloj").authenticated()
                        .requestMatchers("/admin/usuarios/ver/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/relojes/**").hasAnyRole("ADMIN", "TECNICO", "CONSULTOR")
                        .requestMatchers("/historiales/**").hasAnyRole("TECNICO","ADMIN","CONSULTOR")
                        .requestMatchers("/documentos/**").hasAnyRole("ADMIN", "TECNICO")
                        .requestMatchers("/historial").hasAnyRole("ADMIN", "TECNICO", "CONSULTOR")
                        .requestMatchers("/login", "/registrarse**","/login?cambioExitoso**", "/cambiar-password**", "/recuperar", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/api/chat").permitAll()
                        .requestMatchers("/inbox/**").authenticated()
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
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
