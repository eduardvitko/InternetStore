package com.example.InternetStore.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column (name = "name")
    private String name;
}
