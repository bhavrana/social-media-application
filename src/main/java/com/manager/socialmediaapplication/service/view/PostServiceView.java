package com.manager.socialmediaapplication.service.view;

import com.manager.socialmediaapplication.dto.request.PostCreationRequest;
import com.manager.socialmediaapplication.dto.request.UpdatePostRequest;
import com.manager.socialmediaapplication.dto.response.GetPostResponse;
import com.manager.socialmediaapplication.dto.response.GetPostsResponse;

public interface PostServiceView {
    void createPost(PostCreationRequest request);

    GetPostsResponse getPosts();

    GetPostResponse getPost(Long id);

    GetPostResponse updatePost(long postId, UpdatePostRequest request);

    void deletePost(Long id);
}
