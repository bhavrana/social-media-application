package com.manager.socialmediaapplication.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@AllArgsConstructor
@FieldNameConstants(level = AccessLevel.PRIVATE)
public class UpdateCommentRequest {
    @Size(min = 1, max = 2500, message = "Please post the content in range if [1, 100]")
    @NotBlank(message = "Please provide the post content")
    String updatedContent;
}
