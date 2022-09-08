package com.eya.multithreading.executor.api.controller;

import com.eya.multithreading.executor.api.model.User;
import com.eya.multithreading.executor.api.servie.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity saveUser(@RequestParam(value = "files") MultipartFile[] file) throws Exception {

        for (MultipartFile multipartFile : file) {
            userService.saveUsers(multipartFile);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public CompletableFuture<ResponseEntity> GetAllUsers() {

        return userService.findAllUser().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value = "/usersByThreads", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getUser() {
        CompletableFuture<List<User>> users1 = userService.findAllUser();
        CompletableFuture<List<User>> users2 = userService.findAllUser();
        CompletableFuture<List<User>> users3 = userService.findAllUser();
        CompletableFuture.allOf(users1, users2, users3).join();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
