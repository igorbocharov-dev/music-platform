package org.musicplatform.auth.controller;


import org.musicplatform.auth.dto.user.UserMainResponse;
import org.musicplatform.auth.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/profile")
    public UserMainResponse profile(@AuthenticationPrincipal Long userId){
        return service.mainResponse(userId);
    }
}
