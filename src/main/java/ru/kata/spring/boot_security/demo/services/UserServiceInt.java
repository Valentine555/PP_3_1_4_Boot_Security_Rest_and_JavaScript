package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;

public interface UserServiceInt {
    User findByUserName (String name);
    List<User> findAll();
    User findById(int id);
    void deleteById(int id);
    void saveNew(User user);
    void saveUpdate(User user, String newPassword);
}
