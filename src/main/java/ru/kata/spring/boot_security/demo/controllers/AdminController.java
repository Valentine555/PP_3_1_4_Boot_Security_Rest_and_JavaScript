package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;



@Controller
@RequestMapping("/admin")
public class AdminController {
private final RoleService roleService;
private final UserService userService;

    public AdminController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping
    public String adminPanel (){
        return "adminpanel";
    }

    @GetMapping("/users")
    public String Users(ModelMap model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/new")
    public String newUser (ModelMap modelMap){
        modelMap.addAttribute("user", new User());
        modelMap.addAttribute("allRoles", roleService.findAll());
        return "new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("user") User user) {
        userService.saveNew(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/id")
    public String user(@RequestParam("id") int id, ModelMap model){
        model.addAttribute("user", userService.findById(id));
        return "userforadmin";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") int id, ModelMap model){
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("allRoles", roleService.findAll());
        return "edit";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute("user") User user, @RequestParam("password") String newPassword) {
        userService.saveUpdate(user, newPassword);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}
