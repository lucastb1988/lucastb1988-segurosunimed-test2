package com.example.api.web.rest;

import com.example.api.domain.security.User;
import com.example.api.service.security.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = {"API dedicada a criar usuários."})
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("Criação de usuário.")
    @PostMapping
    public ResponseEntity<User> criar(@Valid @RequestBody User obj) {
        userService.create(obj);
        return ResponseEntity.status(201).build();
    }
}
