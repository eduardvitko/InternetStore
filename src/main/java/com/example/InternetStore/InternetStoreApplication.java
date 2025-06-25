package com.example.InternetStore;

import com.example.InternetStore.model.Role;
import com.example.InternetStore.model.User;
import com.example.InternetStore.reposietories.RoleRepository;
import com.example.InternetStore.reposietories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class InternetStoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(InternetStoreApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner init(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder) {
//		return args -> {
//			if (userRepo.findByUsername("admin").isEmpty()) {
//				Role adminRole = roleRepo.findByName("ADMIN")
//						.orElseGet(() -> roleRepo.save(new Role("ADMIN")));
//
//				User admin = new User();
//				admin.setUsername("admin");
//				admin.setEmail("d@example.com");
//				admin.setPhone("+380664521789");
//				admin.setPasswordHash(encoder.encode("eduard"));
//				admin.setRoles(Set.of(adminRole));
//
//				userRepo.save(admin);
//			}
//		};
	}


