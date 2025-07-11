package com.example.InternetStore.reposietories;
import com.example.InternetStore.model.Order;
import com.example.InternetStore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Integer userId);
    List<Order> findAllByUser_Id(Integer userId);


    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.user u " +
            "LEFT JOIN FETCH o.address a " +
            "LEFT JOIN FETCH o.items i " +
            "LEFT JOIN FETCH i.product p")
    List<Order> findAllWithUserAndAddress();

}




