package com.agenciaviagens.destinos_api.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.agenciaviagens.destinos_api.model.Usuario;
import com.agenciaviagens.destinos_api.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository, PasswordEncoder encoder) {
        return args -> {
            if (repository.count() == 0) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("1234"));
                admin.setRole("ADMIN");
                repository.save(admin);

                Usuario user = new Usuario();
                user.setUsername("user");
                user.setPassword(encoder.encode("1234"));
                user.setRole("USER");
                repository.save(user);
            }
        };
    }
} 
