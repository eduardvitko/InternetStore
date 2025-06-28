package com.example.InternetStore.model;

import jakarta.persistence.*;

import java.util.Set;


@Entity
@Table(name = "roles")
public class Role {
    // ✅ ОБЯЗАТЕЛЬНО: пустой конструктор
    public Role() {
    }
    public Role(String name) {
        this.name = name;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;



    public String getName() {
        return name;
    }
}