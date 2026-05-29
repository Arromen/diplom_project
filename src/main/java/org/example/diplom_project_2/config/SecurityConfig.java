package org.example.diplom_project_2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF, чтобы обычные HTML-формы работали без генерации токенов
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Открытые эндпоинты для гостей
                        .requestMatchers("/css/**", "/js/**", "/search/**", "/schedule/**", "/api/**", "/login", "/logout").permitAll()
                        // Ограничение на создание групп (Только Администратор)
                        .requestMatchers(HttpMethod.POST, "/groups").hasRole("ADMIN")
                        // Ограничение на бронирование (Преподаватель и Администратор)
                        .requestMatchers("/bookings/**").hasAnyRole("TEACHER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/schedule", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/schedule")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Позволяет корректно обрабатывать префикс {noop} в базе данных
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}