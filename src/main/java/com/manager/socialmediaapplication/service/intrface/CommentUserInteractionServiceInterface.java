package com.manager.socialmediaapplication.service.intrface;

import com.manager.socialmediaapplication.dto.response.GetCommentResponse;
import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;

public interface CommentUserInteractionServiceInterface {
    GetCommentResponse likeComment(Long commentId, Long userId);

    GetCommentResponse dislikeComment(Long commentId, Long userId);

    GetEndUsersResponse getUserWhoLikedCommentById(Long commentId, Integer pageNo, Integer pageSize);

    GetEndUsersResponse getUserWhoDislikedCommentById(Long commentId, Integer pageNo, Integer pageSize);

    GetCommentResponse removeLikeComment(long commentId, long userId);

    GetCommentResponse removeDislikeComment(long commentId, long userId);
}
