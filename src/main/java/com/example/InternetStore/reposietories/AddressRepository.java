package com.example.InternetStore.reposietories;

import com.example.InternetStore.model.Address;
import com.example.InternetStore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByUser(User user);
}
