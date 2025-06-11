package com.myShop.controller;


import com.myShop.entity.User;
import com.myShop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

        private final UserService  userService;

        @GetMapping("/profile")
        public ResponseEntity<User> createUserHandler(
                @RequestHeader("Authorization") String jwt
        ) throws Exception {


            User user=userService.findUserByJwtToken(jwt);
            return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
        }
}
