package com.example.InternetStore.reposietories;
import com.example.InternetStore.model.Order;
import com.example.InternetStore.model.Product;
import com.example.InternetStore.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Integer userId);
    List<Order> findAllByUser_Id(Integer userId);
    List<Order> findByUser(User user);


    @EntityGraph(attributePaths = {"user", "address", "items", "items.product"})
    @Query("SELECT o FROM Order o")
    List<Order> findAllWithUserAndAddressByUserId();


}




