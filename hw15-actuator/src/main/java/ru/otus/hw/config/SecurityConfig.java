package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity (
    securedEnabled = true
)
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/", "/books").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/books/new", "/books/edit/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/books/*").hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/book", "/api/book/*").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/book").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/book").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/book/*").hasAnyRole("ADMIN")

                        .requestMatchers("/comments/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/comment", "/api/comment/*").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/actuator/**").hasAnyRole("ADMIN")
                        .requestMatchers("/datarest/**").hasAnyRole("ADMIN")
                        .requestMatchers("/explorer/**").hasAnyRole("ADMIN")

                        .anyRequest().denyAll()
                )
                .formLogin(Customizer.withDefaults());
        return http.build();
    }
}
