package com.manager.socialmediaapplication.dto.response;

import com.manager.socialmediaapplication.model.projection.CommentProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetCommentsResponse {
    Page<CommentProjection> response;
}
