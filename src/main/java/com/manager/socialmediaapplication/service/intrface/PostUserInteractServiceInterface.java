package com.manager.socialmediaapplication.service.intrface;

import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;

public interface PostUserInteractServiceInterface {
    void likePost(Long postId, Long userId);

    void dislikePost(Long postId, Long userId);

    GetEndUsersResponse getUserWhoLikedPostById(Long postId, Integer pageNo, Integer pageSize);

    GetEndUsersResponse getUserWhoDislikedPostById(Long postId, Integer pageNo, Integer pageSize);

    void removeLikePost(long postId, long userId);

    void removeDislikePost(long postId, long userId);
}
