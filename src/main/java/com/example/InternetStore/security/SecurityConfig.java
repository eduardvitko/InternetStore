package com.example.InternetStore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // <-- ВАЖЛИВО: Імпортуйте HttpMethod
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Використовуємо CorsConfigurationSource, визначений нижче
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Вимикаємо CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // Налаштовуємо правила авторизації
                .authorizeHttpRequests(auth -> auth
                        // Дозволяємо всі OPTIONS запити (вирішує проблему з preflight)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Дозволяємо доступ до ваших ендпоінтів автентифікації
                        .requestMatchers("/api/auth/**").permitAll()// <-- Я змінив це на /auth/**, як у ваших помилках. Якщо треба /api/auth/**, поверніть як було.

                        // Всі інші запити вимагають автентифікації
                        .anyRequest().authenticated()
                )
                // Встановлюємо політику сесій на STATELESS, оскільки ми використовуємо JWT
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Додаємо наш провайдер автентифікації
                .authenticationProvider(authProvider())
                // Додаємо наш JWT фільтр перед стандартним фільтром
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Вказуємо дозволені домени (ВАШ САЙТ І LOCALHOST)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "https://edinternetshop.netlify.app"
        ));
        // Дозволяємо всі методи (GET, POST, PUT, DELETE, OPTIONS і т.д.)
        configuration.setAllowedMethods(List.of("*"));
        // Дозволяємо всі заголовки (Authorization, Content-Type і т.д.)
        configuration.setAllowedHeaders(List.of("*"));
        // Дозволяємо передачу credentials
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

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
