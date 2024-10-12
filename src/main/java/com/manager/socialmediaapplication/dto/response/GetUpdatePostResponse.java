package com.manager.socialmediaapplication.dto.response;

import com.manager.socialmediaapplication.model.projection.PostProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetUpdatePostResponse {
    PostProjection post;
}
