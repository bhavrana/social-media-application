package com.manager.socialmediaapplication.controller;

import com.manager.socialmediaapplication.dto.request.EndUserCreationRequest;
import com.manager.socialmediaapplication.dto.response.GetEndUserResponse;
import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;
import com.manager.socialmediaapplication.service.intrface.EndUserServiceInterface;
import com.manager.socialmediaapplication.service.validation.EndUserValidationService;
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
    EndUserServiceInterface endUserServiceInterface;
    EndUserValidationService endUserValidationService;

    @Autowired
    public UserController(EndUserServiceInterface endUserServiceInterface, EndUserValidationService endUserValidationService) {
        this.endUserServiceInterface = endUserServiceInterface;
        this.endUserValidationService = endUserValidationService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createEndUser(@Valid @RequestBody EndUserCreationRequest endUserCreationRequest) {
        endUserServiceInterface.createEndUser(endUserCreationRequest);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<GetEndUsersResponse> getEndUsers(@RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize,
                                                           @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                           @RequestParam(value = "sorting", defaultValue = "DESC", required = false) String order) {
        return ResponseEntity.ok(endUserServiceInterface.getEndUsers(pageNo, pageSize, order));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<GetEndUserResponse> getEndUserById(@PathVariable long userId) {
        endUserValidationService.doesUserExist(userId);
        return ResponseEntity.ok(endUserServiceInterface.getEndUserById(userId));
    }
}
