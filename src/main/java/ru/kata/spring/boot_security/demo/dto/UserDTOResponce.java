package ru.kata.spring.boot_security.demo.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.HashSet;
import java.util.Set;

public class UserDTOResponce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов")
    private String name;

    @NotEmpty(message = "Фамилия не должна быть пустой")
    @Size(min = 2, max = 30, message = "Фамилия должна быть от 2 до 30 символов")
    private String surname;


    private int year;

    @Email
    @NotEmpty
    private String email;


    private Set<Role> roles = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotEmpty(message = "Имя не должно быть пустым") @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "Имя не должно быть пустым") @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов") String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public @NotEmpty(message = "Фамилия не должна быть пустой") @Size(min = 2, max = 30, message = "Фамилия должна быть от 2 до 30 символов") String getSurname() {
        return surname;
    }

    public void setSurname(@NotEmpty(message = "Фамилия не должна быть пустой") @Size(min = 2, max = 30, message = "Фамилия должна быть от 2 до 30 символов") String surname) {
        this.surname = surname;
    }

    public @Email @NotEmpty String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotEmpty String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
