package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;



@Controller
@RequestMapping("/adminpanel")
public class AdminController {
private final RoleService roleService;
private final UserService userService;

    public AdminController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public String adminPanel (ModelMap model, Authentication authentication){
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", userService.findByUserEmail(authentication.getName()));
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("newUser", new User());
        model.addAttribute("editUser", new User());
        return "adminpanel";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("newUser") User user) {
        userService.saveNew(user);
        return "redirect:/adminpanel";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("editUser") User user, @RequestParam("password") String newPassword) {
        userService.saveUpdate(user, newPassword);
        return "redirect:/adminpanel";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam ("id") int id) {
        userService.deleteById(id);
        return "redirect:/adminpanel";
    }
}
