package com.manager.socialmediaapplication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndUserCreationRequest {

    @NotBlank(message = "Please provide name")
    @Size(max = 100, min = 1, message = "Please provide valid name in character range of [1, 100]")
    String name;

    @Email
    @NotBlank(message = "Please provide email")
    String email;
}
