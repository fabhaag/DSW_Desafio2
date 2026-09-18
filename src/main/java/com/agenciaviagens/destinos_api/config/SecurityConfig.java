package com.agenciaviagens.destinos_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// A anotação @Cconfiguration indica que esta classe é uma classe de configuração do Spring, permitindo a definição de beans e configurações específicas para a aplicação.
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    // A anotação @Bean indica que o método securityFilterChain() retorna um bean gerenciado pelo Spring, que é responsável por configurar a segurança da aplicação.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Consultas públicas
                .requestMatchers(HttpMethod.GET, "/destinos/**").permitAll()
                // Apenas ADMIN pode cadastrar, atualizar ou excluir
                .requestMatchers(HttpMethod.POST, "/destinos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/destinos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/destinos/**").hasRole("ADMIN")
                // Usuários autenticados (USER ou ADMIN) podem avaliar
                .requestMatchers(HttpMethod.PATCH, "/destinos/*/avaliar").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults()); // Utiliza autenticação básica para simplificar testes

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}