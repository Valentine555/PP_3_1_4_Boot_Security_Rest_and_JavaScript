package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTORequest;
import ru.kata.spring.boot_security.demo.dto.UserDTOResponce;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;
import ru.kata.spring.boot_security.demo.util.UserNotUpdatedException;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class APIController {
    private final RoleService roleService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public APIController(RoleService roleService, UserService userService, ModelMapper modelMapper) {
        this.roleService = roleService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/users")
    public List<UserDTOResponce> getUsers() {
        return userService.findAll().stream().map(this::convertToUserDTOResponse).collect(Collectors.toList());
    }


    @GetMapping("/users/{id}")
    public UserDTOResponce getUser(@PathVariable("id") int id) {
        return convertToUserDTOResponse(userService.findById(id));
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable("id") int id, @RequestBody @Valid UserDTORequest userDTORequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            StringBuilder errorMsg=new StringBuilder();
            List <FieldError> errors= (List<FieldError>) bindingResult.getFieldError();
            for (FieldError error: errors){
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage()).append(";");

            }
            throw new UserNotUpdatedException(errorMsg.toString());

        }
        userDTORequest.setId(id);
        userService.saveUpdate(convertToUser(userDTORequest));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/viewUser")
    public User showUser(Principal principal){
       return userService.findByUserEmail(principal.getName());
    }

    @GetMapping("/roles")
    public Set<Role> getAllRoles() {
        return roleService.findAll();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        userService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PostMapping("/users")
    public ResponseEntity<HttpStatus> create (@RequestBody @Valid UserDTORequest userDTORequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            StringBuilder errorMsg=new StringBuilder();
            List <FieldError> errors= (List<FieldError>) bindingResult.getFieldError();
            for (FieldError error: errors){
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage()).append(";");

            }
            throw new UserNotCreatedException(errorMsg.toString());

        }
        userService.saveNew(convertToUser(userDTORequest));
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e){
        UserErrorResponse response=new UserErrorResponse("Пользователя с таким id не существует", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e){
        UserErrorResponse response=new UserErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private User convertToUser(UserDTORequest userDTORequest) {
        return modelMapper.map(userDTORequest, User.class);
    }

    private UserDTOResponce convertToUserDTOResponse(User user){
        return modelMapper.map(user,UserDTOResponce.class);
    }

}