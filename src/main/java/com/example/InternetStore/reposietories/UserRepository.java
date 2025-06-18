package com.example.InternetStore.reposietories;

import com.example.InternetStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

}
