package com.manager.socialmediaapplication.controller;

import com.manager.socialmediaapplication.dto.request.CommentCreationRequest;
import com.manager.socialmediaapplication.dto.request.UpdateCommentRequest;
import com.manager.socialmediaapplication.dto.response.GetCommentResponse;
import com.manager.socialmediaapplication.dto.response.GetCommentsResponse;
import com.manager.socialmediaapplication.exception.CommentNotFoundException;
import com.manager.socialmediaapplication.exception.InvalidCommentPostException;
import com.manager.socialmediaapplication.exception.PostNotFoundException;
import com.manager.socialmediaapplication.exception.UserNotFoundException;
import com.manager.socialmediaapplication.model.projection.CommentProjection;
import com.manager.socialmediaapplication.service.implementation.CommentService;
import com.manager.socialmediaapplication.service.validation.CommentValidationService;
import com.manager.socialmediaapplication.service.validation.EndUserValidationService;
import com.manager.socialmediaapplication.service.validation.PostValidationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentControllerTest {
    @InjectMocks
    CommentController commentController;

    @Mock
    EndUserValidationService endUserValidationService;
    @Mock
    PostValidationService postValidationService;
    @Mock
    CommentValidationService commentValidationService;
    @Mock
    CommentService commentService;

    @Test
    void test_createComment_Success() {
        Long commentId = 1L;
        CommentProjection commentProjection = new CommentProjection() {
            @Override
            public Long getId() {
                return commentId;
            }

            @Override
            public String getContent() {
                return "Test content";
            }

            @Override
            public LocalDateTime getCreatedDate() {
                return LocalDateTime.now();
            }

            @Override
            public String getEndUserName() {
                return "Test User";
            }

            @Override
            public Long getLikeCount() {
                return 0L;
            }

            @Override
            public Long getDislikeCount() {
                return 0L;
            }
        };

        GetCommentResponse response = new GetCommentResponse();
        response.setCommentProjection(commentProjection);
        CommentCreationRequest request = new CommentCreationRequest("comment", 1L, 1L, null);
        doNothing().when(endUserValidationService).doesUserExist(request.getUserId());
        doNothing().when(postValidationService).doesPostExist(request.getPostId());
        doNothing().when(commentValidationService).isPostValid(request.getPostId(), request.getParentId());
        when(commentService.createComment(request)).thenReturn(response);
        ResponseEntity<GetCommentResponse> responseEntity = commentController.createComment(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(commentProjection, response.getCommentProjection());
    }

    @Test
    void test_createComment_WithNonExistingUser() {
        CommentCreationRequest request = new CommentCreationRequest("comment", 1L, 1L, null);
        doThrow(new UserNotFoundException("User not found for Id " + request.getUserId())).when(endUserValidationService).doesUserExist(request.getUserId());

        assertThrows(UserNotFoundException.class, () -> commentController.createComment(request));
    }

    @Test
    void test_createComment_WithNonExistingPost() {
        CommentCreationRequest request = new CommentCreationRequest("comment", 1L, 1L, null);
        doNothing().when(endUserValidationService).doesUserExist(request.getUserId());
        doThrow(new PostNotFoundException("Post not found for Id " + request.getPostId())).when(postValidationService).doesPostExist(request.getPostId());

        assertThrows(PostNotFoundException.class, () -> commentController.createComment(request));
    }

    @Test
    void test_createComment_WithNonExistingComment() {
        CommentCreationRequest request = new CommentCreationRequest("comment", 1L, 1L, null);
        doNothing().when(endUserValidationService).doesUserExist(request.getUserId());
        doNothing().when(postValidationService).doesPostExist(request.getPostId());
        doThrow(new CommentNotFoundException("Comment with id " + request.getParentId() + " does not exist")).when(commentValidationService).isPostValid(request.getPostId(), request.getParentId());

        assertThrows(CommentNotFoundException.class, () -> commentController.createComment(request));
    }

    @Test
    void test_createComment_WithNonExistingCommentParent() {
        CommentCreationRequest request = new CommentCreationRequest("comment", 1L, 1L, null);
        doNothing().when(endUserValidationService).doesUserExist(request.getUserId());
        doNothing().when(postValidationService).doesPostExist(request.getPostId());
        doThrow(new InvalidCommentPostException("Post ID of the parent: " + request.getParentId() + " is different from given post ID: " + request.getPostId())).when(commentValidationService).isPostValid(request.getPostId(), request.getParentId());

        assertThrows(InvalidCommentPostException.class, () -> commentController.createComment(request));
    }

    @Test
    void test_updateComment_Success() {
        long commentId = 1L;
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setUpdatedContent("Updated");
        doNothing().when(commentValidationService).doesCommentExist(commentId);
        GetCommentResponse response = new GetCommentResponse();
        when(commentService.updateComment(commentId, request)).thenReturn(response);

        ResponseEntity<GetCommentResponse> result = commentController.updateComment(commentId, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void test_updateComment_WithNonExistingComment() {
        long commentId = 1L;
        UpdateCommentRequest request = new UpdateCommentRequest();
        request.setUpdatedContent("Updated");
        doThrow(new CommentNotFoundException("Comment not found for Id " + commentId)).when(commentValidationService).doesCommentExist(commentId);

        assertThrows(CommentNotFoundException.class, () -> commentController.updateComment(commentId, request));
    }

    @Test
    void test_getCommentsForPost_Success() {
        long postId = 1L;
        Integer pageSize = 3;
        Integer pageNo = 0;
        String order = "DESC";
        doNothing().when(postValidationService).doesPostExist(postId);
        GetCommentsResponse response = new GetCommentsResponse();
        when(commentService.getCommentsForPost(postId, pageSize, pageNo, order)).thenReturn(response);

        ResponseEntity<GetCommentsResponse> result = commentController.getCommentsForPost(postId, pageSize, pageNo, order);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void test_getCommentsForPost_NonExistentPost() {
        long postId = 1L;
        Integer pageSize = 3;
        Integer pageNo = 0;
        String order = "DESC";
        doThrow(new PostNotFoundException("Post not found for Id " + postId)).when(postValidationService).doesPostExist(postId);

        assertThrows(PostNotFoundException.class, () -> commentController.getCommentsForPost(postId, pageSize, pageNo, order));
    }

    @Test
    void test_getCommentsForParentComment_Success() {
        long commentId = 1L;
        Integer pageSize = 3;
        Integer pageNo = 0;
        String order = "DESC";
        doNothing().when(commentValidationService).doesCommentExist(commentId);
        GetCommentsResponse response = new GetCommentsResponse();
        when(commentService.getCommentsForParent(commentId, pageSize, pageNo, order)).thenReturn(response);

        ResponseEntity<GetCommentsResponse> result = commentController.getCommentsForParentComment(commentId, pageSize, pageNo, order);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void test_getCommentsForParentComment_NonExistentComment() {
        long commentId = 1L;
        Integer pageSize = 3;
        Integer pageNo = 0;
        String order = "DESC";
        doThrow(new PostNotFoundException("Comment not found for Id " + commentId)).when(commentValidationService).doesCommentExist(commentId);

        assertThrows(PostNotFoundException.class, () -> commentController.getCommentsForParentComment(commentId, pageSize, pageNo, order));
    }
}
