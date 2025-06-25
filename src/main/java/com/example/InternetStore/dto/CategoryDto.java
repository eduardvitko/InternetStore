package com.example.InternetStore.dto;

import com.example.InternetStore.model.Category;
import com.example.InternetStore.model.Role;
import com.example.InternetStore.model.User;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;
@Data
public class CategoryDto {

    private Integer id;
    private String name;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();

    }
    public CategoryDto(Integer id, String name) {
        this.id = id;
        this.name = name;

    }
    public Integer getId(){return id;}
    public String getName(){return name;}
}
