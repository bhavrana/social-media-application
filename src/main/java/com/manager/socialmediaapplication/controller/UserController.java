package com.manager.socialmediaapplication.controller;

import com.manager.socialmediaapplication.dto.request.EndUserCreationRequest;
import com.manager.socialmediaapplication.dto.response.GetEndUserResponse;
import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;
import com.manager.socialmediaapplication.service.view.EndUserServiceView;
import jakarta.validation.Valid;
import lombok.AccessLevel;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    EndUserServiceView endUserServiceView;

    @Autowired
    public UserController(EndUserServiceView endUserServiceView) {
        this.endUserServiceView = endUserServiceView;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createEndUser(@Valid @RequestBody EndUserCreationRequest endUserCreationRequest) {
        endUserServiceView.createEndUser(endUserCreationRequest);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<GetEndUsersResponse> getEndUsers() {
        return ResponseEntity.ok(endUserServiceView.getEndUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GetEndUserResponse> getEndUsers(@PathVariable long userId) {
        return ResponseEntity.ok(endUserServiceView.getEndUserById(userId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteEndUser(@PathVariable long userId) {
        endUserServiceView.deleteEndUserById(userId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
