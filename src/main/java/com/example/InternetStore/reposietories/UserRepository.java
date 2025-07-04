package com.example.InternetStore.reposietories;

import com.example.InternetStore.dto.UserDto;
import com.example.InternetStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    Optional<User> findById(int id);
    Optional<User> findByPhone(String phone);

  // private void deleteById(int id) {

    //}
}
