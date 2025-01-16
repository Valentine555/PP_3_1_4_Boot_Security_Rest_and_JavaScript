package ru.kata.spring.boot_security.demo.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.UserDetails.UserDetailsImp;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService, UserServiceInt {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

public User findByUserEmail(String email){
    return userRepository.findByEmail(email);
}

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userRepository.findByEmail(email);
        if (user==null){
            throw new UsernameNotFoundException("User not found");
        }

        return new UserDetailsImp(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }


    @Transactional
    public void saveNew(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = user.getRoles();
        Set<Role> managedRoles = new HashSet<>();
        for (Role role : roles) {
            Role existingRole = roleRepository.findByName(role.getName());
            if (existingRole != null) {
                managedRoles.add(existingRole);
            } else {
                throw new RuntimeException("Роль не найдена: " + role.getName());
            }
        }
        user.setRoles(managedRoles);
        userRepository.save(user);
    }

    @Transactional
    public void saveUpdate(User user) {
            User existingUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found " + user.getId()));
            if (user.getPassword() == null || user.getPassword().isEmpty() || passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                user.setPassword(existingUser.getPassword());
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));

            }
        Set<Role> roles = user.getRoles();
        Set<Role> managedRoles = new HashSet<>();
        for (Role role : roles) {
            Role existingRole = roleRepository.findByName(role.getName());
            if (existingRole != null) {
                managedRoles.add(existingRole);
            } else {
                throw new RuntimeException("Роль не найдена: " + role.getName());
            }
        }
        user.setRoles(managedRoles);
        userRepository.save(user);
    }

}
