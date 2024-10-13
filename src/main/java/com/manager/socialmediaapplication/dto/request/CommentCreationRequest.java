package com.manager.socialmediaapplication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CommentCreationRequest {
    @Size(min = 1, max = 2500, message = "Please comment the content in range if [1, 100]")
    @NotBlank(message = "Please provide the comment content")
    String content;

    @NotNull(message = "Null value is not supported for User ID")
    Long userId;

    @NotNull(message = "Null value is not supported for Post ID")
    Long postId;

    Long parentId;
}
