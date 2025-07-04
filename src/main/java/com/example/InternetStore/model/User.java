package com.example.InternetStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @NotBlank(message = "Ім'я користувача не може бути порожнім")
    @Size(min = 3, max = 30, message = "Ім'я має бути від 3 до 30 символів")
//    @Pattern(
//            regexp = "^[a-zA-Zа-яА-ЯіІїЇєЄґҐ]+$",
//            message = "Ім’я користувача може містити лише літери українського або англійського алфавіту"
//    )
    @Column(name = "username", nullable = false)
    private String username;

//    @NotBlank(message = "Email не може бути порожнім")
//    @Email(message = "Невірний формат email")
    @Column(name = "email")
    private String email;

//    @Pattern(
//            regexp = "\"\\\\+38\\\\d{10}\"",
//            message = "Телефон має містити тільки цифри та може починатись з +"
//    )
    @Column (name = "phone")
    private String phone;

    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 8, message = "Пароль має містити щонайменше 8 символів")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}