package com.example.InternetStore.security;

import com.example.InternetStore.model.Role;
import com.example.InternetStore.model.User;
import com.example.InternetStore.reposietories.RoleRepository;
import com.example.InternetStore.reposietories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Всередині вашого класу AuthController.java

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        // Перевірка на дублікат телефону
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            // Створюємо Map для відповіді з помилкою
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Користувач з таким телефоном вже існує.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        // Перевірка на дублікат імені користувача
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Користувач з таким ім'ям вже існує.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        // Створення нового користувача
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        newUser.setPhone(request.getPhone());

        // Логіка призначення ролі (без змін, вона правильна)
        final String ADMIN_PHONE_NUMBER = "+380637097311";
        if (ADMIN_PHONE_NUMBER.equals(request.getPhone())) {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ADMIN")));
            newUser.setRoles(Set.of(adminRole));
        } else {
            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(new Role("USER")));
            newUser.setRoles(Set.of(userRole));
        }

        // Зберігаємо нового користувача в базу даних
        userRepository.save(newUser);

        // Після успішного збереження, генеруємо для нього токен
        // Переконайтесь, що ваш JwtUtil має метод generateToken, який приймає String (username)
        final String token = jwtUtil.generateToken(newUser.getUsername());

        // Повертаємо токен на фронтенд у вигляді об'єкта AuthResponse
        return ResponseEntity.ok(new AuthResponse(token));
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
