package com.example.watchtracker;

import jakarta.servlet.annotation.MultipartConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // 👈 Asegúrate de importar esto
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.watchtracker") // 👈 Esto es clave
@MultipartConfig
public class WatchtrackerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(WatchtrackerApplication.class, args);
	}
}

