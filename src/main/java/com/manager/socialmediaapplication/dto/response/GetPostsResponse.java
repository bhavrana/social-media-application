package com.manager.socialmediaapplication.dto.response;

import com.manager.socialmediaapplication.model.projection.PostProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetPostsResponse {
    Page<PostProjection> response;
}
