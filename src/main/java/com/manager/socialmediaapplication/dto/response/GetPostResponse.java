package com.manager.socialmediaapplication.dto.response;

import com.manager.socialmediaapplication.model.projection.PostProjection;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetPostResponse {
    PostProjection postProjection;
}
