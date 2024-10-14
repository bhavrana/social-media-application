package com.manager.socialmediaapplication.validation;

import com.manager.socialmediaapplication.exception.PostNotFoundException;
import com.manager.socialmediaapplication.repository.PostRepository;
import com.manager.socialmediaapplication.service.validation.PostValidationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostValidationServiceTest {
    @InjectMocks
    PostValidationService postValidationService;

    @Mock
    private PostRepository postRepository;

    @Test
    void test_doesPostExist_PostExists() {
        long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(true);

        postValidationService.doesPostExist(postId);
    }

    @Test
    void test_doesPostExist_PostDoesNotExist() {
        long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(false);
        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> postValidationService.doesPostExist(postId));
        String expectedMessage = "Post not found for Id " + postId;
        assert expectedMessage.equals(exception.getMessage());
    }
}
