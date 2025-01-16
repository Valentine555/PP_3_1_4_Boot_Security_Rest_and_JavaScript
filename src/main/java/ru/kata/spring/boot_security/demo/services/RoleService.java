package ru.kata.spring.boot_security.demo.services;


import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleService implements RoleServiceInt {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Set<Role> findAll() {
       return new HashSet<>(roleRepository.findAll());
    }
}
