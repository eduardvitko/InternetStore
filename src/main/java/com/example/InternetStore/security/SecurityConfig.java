package com.example.InternetStore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    @Order(1)
    public SecurityFilterChain publicEndpoints(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/auth/**", "/api/categories/**", "/api/products/**") // Обробляємо тільки ці шляхи
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Дозволяємо абсолютно всі запити, що підпали під securityMatcher
                );
        return http.build();
    }
    // ↑↑↑↑↑↑  КІНЕЦЬ НОВОГО БЛОКУ  ↑↑↑↑↑↑


    // ↓↓↓↓↓↓  ОНОВЛЕНИЙ БЛОК ДЛЯ ЗАХИЩЕНИХ МАРШРУТІВ  ↓↓↓↓↓↓
    /**
     * Цей ланцюжок фільтрів має нижчий пріоритет (Order(2)) і обробляє
     * всі ІНШІ маршрути, застосовуючи до них JWT-аутентифікацію.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain protectedEndpoints(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Правила для авторизованих користувачів
                        .requestMatchers("/api/user/me", "/api/addresses/**", "/api/orders/**", "/api/payments/**").authenticated()
                        // Правила для адмінів
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/images").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/images/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/images/**").hasRole("ADMIN")
                        // Всі інші запити, що дійшли сюди, вимагають автентифікації
                        .anyRequest().authenticated()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "https://edinternetshop.netlify.app"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Решта ваших бінів
    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}