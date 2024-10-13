package com.manager.socialmediaapplication.model.projection;

import java.time.LocalDateTime;

public interface CommentProjection {
    Long getId();
    String getContent();
    LocalDateTime getCreatedDate();
    String getEndUserName();
    Long getLikeCount();
    Long getDislikeCount();
}
