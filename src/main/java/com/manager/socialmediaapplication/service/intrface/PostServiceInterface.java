package com.manager.socialmediaapplication.service.intrface;

import com.manager.socialmediaapplication.dto.request.PostCreationRequest;
import com.manager.socialmediaapplication.dto.request.UpdatePostRequest;
import com.manager.socialmediaapplication.dto.response.GetPostResponse;
import com.manager.socialmediaapplication.dto.response.GetPostsResponse;

public interface PostServiceInterface {
    void createPost(PostCreationRequest request);

    GetPostsResponse getPosts(Integer pageSize, Integer pageNo, String orderBy);

    GetPostResponse getPost(Long id);

    GetPostResponse updatePost(long postId, UpdatePostRequest request);

    GetPostsResponse getPostForUserId(long userId, Integer pageSize, Integer pageNo, String orderBy);
}
