package com.manager.socialmediaapplication.model.projection;

import com.manager.socialmediaapplication.model.EndUser;
import java.time.LocalDateTime;

public interface CommentProjection {
    Long getId();
    String getContent();
    EndUser getEndUser();
    LocalDateTime getCreatedDate();
}
