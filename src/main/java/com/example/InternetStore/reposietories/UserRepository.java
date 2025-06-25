package com.example.InternetStore.reposietories;

import com.example.InternetStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<Object> findByUsername(String username);
    Optional<Object> findById(int id);
    Optional<Object> findByPhone(String phone);
   // private void deleteById(int id) {

    //}
}
