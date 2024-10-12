package com.manager.socialmediaapplication.service.view;

import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;
import com.manager.socialmediaapplication.dto.response.GetPostResponse;

public interface PostUserInteractServiceView {
    GetPostResponse likePost(Long postId, Long userId);

    GetPostResponse dislikePost(Long postId, Long userId);

    GetEndUsersResponse getUserWhoLikedPostById(Long postId);

    GetEndUsersResponse getUserWhoDislikedPostById(Long postId);
}
