package com.manager.socialmediaapplication.validation;

import com.manager.socialmediaapplication.exception.CommentNotFoundException;
import com.manager.socialmediaapplication.exception.InvalidCommentPostException;
import com.manager.socialmediaapplication.model.Comment;
import com.manager.socialmediaapplication.model.Post;
import com.manager.socialmediaapplication.repository.CommentRepository;
import com.manager.socialmediaapplication.service.validation.CommentValidationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentValidationServiceTest {
    @InjectMocks
    CommentValidationService commentValidationService;

    @Mock
    private CommentRepository commentRepository;

    @Test
    void test_doesCommentExist_CommentExists() {
        Long id = 1L;
        when(commentRepository.existsById(id)).thenReturn(true);
        commentValidationService.doesCommentExist(id);
    }

    @Test
    void test_doesCommentExist_CommentDoesNotExist() {
        Long id = 1L;
        when(commentRepository.existsById(id)).thenReturn(false);
        CommentNotFoundException exception = assertThrows(CommentNotFoundException.class, () -> commentValidationService.doesCommentExist(id));
        String expectedMessage = "Comment with id " + id + " does not exist";
        assert expectedMessage.equals(exception.getMessage());
    }

    @Test
    void test_isPostValid_ParentIdIsNull() {
        Long postId = 1L;
        Long parentId = null;
        commentValidationService.isPostValid(postId, parentId);
    }

    @Test
    void test_isPostValid_ParentCommentExistsAndPostIdMatches() {
        Long postId = 1L;
        Long parentId = 1L;
        Comment parentComment = new Comment();
        Post post = new Post();
        post.setId(postId);
        parentComment.setPost(post);
        when(commentRepository.existsById(parentId)).thenReturn(true);
        when(commentRepository.findById(parentId)).thenReturn(java.util.Optional.of(parentComment));
        commentValidationService.isPostValid(postId, parentId);
    }

    @Test
    void test_isPostValid_ParentCommentDoesNotExist() {
        Long postId = 1L;
        Long parentId = 1L;
        when(commentRepository.existsById(parentId)).thenReturn(false);
        CommentNotFoundException exception = assertThrows(CommentNotFoundException.class, () -> commentValidationService.isPostValid(postId, parentId));
        String expectedMessage = "Comment with id " + parentId + " does not exist";
        assert expectedMessage.equals(exception.getMessage());
    }

    @Test
    void test_isPostValid_ParentPostIdDoesNotMatch() {
        Long postId = 1L;
        Long parentId = 1L;
        Comment parentComment = new Comment();
        Post post = new Post();
        post.setId(2L);
        parentComment.setPost(post);
        when(commentRepository.existsById(parentId)).thenReturn(true);
        when(commentRepository.findById(parentId)).thenReturn(java.util.Optional.of(parentComment));
        InvalidCommentPostException exception = assertThrows(InvalidCommentPostException.class, () -> commentValidationService.isPostValid(postId, parentId));
        String expectedMessage = "Post ID of the parent: 2 is different from given post ID: " + postId;
        assert expectedMessage.equals(exception.getMessage());
    }
}