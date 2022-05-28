package com.fsy2001.artwork.controller;

import com.fsy2001.artwork.model.User;
import com.fsy2001.artwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/user")
    public void register(@RequestBody User user) {
        userService.register(user);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/recharge")
    public void recharge(@RequestParam(value = "val") Integer val, Principal principal) {
        userService.recharge(val, principal.getName());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    public User getInfo(Principal principal) {
        return userService.getInfo(principal.getName());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/user/avatar")
    public void uploadNewBook(@RequestPart("avatar") MultipartFile multipartFile, Principal principal) {
        userService.setAvatar(multipartFile, principal.getName());
    }
}