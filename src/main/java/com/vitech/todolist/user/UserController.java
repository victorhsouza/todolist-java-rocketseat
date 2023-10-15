package com.vitech.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if(user != null){
            System.out.println("Usuario ja existe");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("erro","Usuario ja existe"));
        }
        var passwordHashred = BCrypt.withDefaults().hashToString(12,userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashred);
       var userCreated =  this.userRepository.save(userModel);
       return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
