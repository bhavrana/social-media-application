package com.manager.socialmediaapplication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostCreationRequest {
    @Size(min = 1, max = 2500, message = "Please post the content in range if [1, 100]")
    @NotBlank(message = "Please provide the post content")
    String content;

    @NotNull(message = "Null value is not supported for User ID")
    Long userId;
}
