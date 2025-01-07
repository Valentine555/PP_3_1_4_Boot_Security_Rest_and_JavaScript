package ru.kata.spring.boot_security.demo.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

        @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.name = :name")
        Optional <User> findByName(String name);

        @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
        User findByEmail(String email);
}
