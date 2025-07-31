package com.example.InternetStore;

import com.example.InternetStore.model.Role;
import com.example.InternetStore.model.User;
import com.example.InternetStore.reposietories.RoleRepository;
import com.example.InternetStore.reposietories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;


@SpringBootApplication
public class InternetStoreApplication {
	private static final Logger logger = LoggerFactory.getLogger(InternetStoreApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(InternetStoreApplication.class, args);
		logger.info("<<<<< ЗАСТОСУНОК ЗАПУЩЕНО З ОСТАННЬОЮ ВЕРСІЄЮ SECURITY CONFIG! >>>>>");
	}


}

