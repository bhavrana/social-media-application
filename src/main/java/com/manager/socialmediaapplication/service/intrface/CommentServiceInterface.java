package com.manager.socialmediaapplication.service.intrface;

import com.manager.socialmediaapplication.dto.request.CommentCreationRequest;
import com.manager.socialmediaapplication.dto.request.UpdateCommentRequest;
import com.manager.socialmediaapplication.dto.response.GetCommentResponse;
import com.manager.socialmediaapplication.dto.response.GetCommentsResponse;

public interface CommentServiceInterface {
    GetCommentResponse createComment(CommentCreationRequest request);

    GetCommentsResponse getCommentsForPost(Long postId, Integer pageSize, Integer pageNumber, String order);

    GetCommentsResponse getCommentsForParent(Long parentCommentId, Integer pageSize, Integer pageNumber, String org);

    GetCommentResponse updateComment(Long commentId, UpdateCommentRequest request);
}
