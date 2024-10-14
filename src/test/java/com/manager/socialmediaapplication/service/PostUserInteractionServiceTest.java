package com.manager.socialmediaapplication.service;

import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;
import com.manager.socialmediaapplication.model.EndUser;
import com.manager.socialmediaapplication.model.Post;
import com.manager.socialmediaapplication.model.PostReaction;
import com.manager.socialmediaapplication.model.PostUserInteraction;
import com.manager.socialmediaapplication.model.projection.EndUserProjection;
import com.manager.socialmediaapplication.repository.EndUserRepository;
import com.manager.socialmediaapplication.repository.PostReactionRepository;
import com.manager.socialmediaapplication.repository.PostRepository;
import com.manager.socialmediaapplication.repository.PostUserInteractionRepository;
import com.manager.socialmediaapplication.service.implementation.PostUserInteractionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostUserInteractionServiceTest {
    @InjectMocks
    PostUserInteractionService postUserInteractionService;
    @Mock
    PostUserInteractionRepository postUserInteractionRepository;
    @Mock
    PostReactionRepository postReactionRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    EndUserRepository endUserRepository;

    @Test
    void test_likePost_WhenActiveEntryExistsAndNotLiked() {
        Long postId = 1L;
        Long userId = 1L;
        PostUserInteraction postUserInteraction = new PostUserInteraction();
        postUserInteraction.setIsLiked(false);
        postUserInteraction.setIsActive(true);
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.of(postUserInteraction));
        PostReaction postReaction = new PostReaction();
        postReaction.setLikeCount(0L);
        postReaction.setDislikeCount(1L);
        when(postReactionRepository.findByPost_Id(postId)).thenReturn(java.util.Optional.of(postReaction));
        postUserInteractionService.likePost(postId, userId);
        assertEquals(1L, postReaction.getLikeCount());
        assertEquals(0L, postReaction.getDislikeCount());
    }

    @Test
    void test_likePost_WhenActiveEntryDoesNotExist() {
        Long postId = 1L;
        Long userId = 1L;
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.empty());
        Post post = new Post();
        post.setId(postId);
        when(postRepository.findById(postId)).thenReturn(java.util.Optional.of(post));
        EndUser endUser = new EndUser();
        endUser.setId(userId);
        when(endUserRepository.findById(userId)).thenReturn(java.util.Optional.of(endUser));
        postUserInteractionService.likePost(postId, userId);
        PostUserInteraction postUserInteraction = new PostUserInteraction();
        postUserInteraction.setPost(post);
        postUserInteraction.setIsLiked(true);
        postUserInteraction.setEndUser(endUser);
        postUserInteraction.setIsActive(true);
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.of(postUserInteraction));
        PostReaction postReaction = new PostReaction();
        postReaction.setPost(post);
        postReaction.setLikeCount(1L);
        postReaction.setDislikeCount(0L);
        when(postReactionRepository.findByPost_Id(postId)).thenReturn(java.util.Optional.of(postReaction));
        assertEquals(1L, postReaction.getLikeCount());
        assertEquals(0L, postReaction.getDislikeCount());
    }

    @Test
    void test_likePost_WhenAlreadyLiked() {
        Long postId = 1L;
        Long userId = 1L;
        PostUserInteraction postUserInteraction = new PostUserInteraction();
        postUserInteraction.setIsLiked(true);
        postUserInteraction.setIsActive(true);
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.of(postUserInteraction));
        when(postRepository.findById(postId)).thenReturn(java.util.Optional.of(new Post()));
        when(endUserRepository.findById(userId)).thenReturn(java.util.Optional.of(new EndUser()));
        postUserInteractionService.likePost(postId, userId);
        PostReaction postReaction = new PostReaction();
        postReaction.setLikeCount(1L);
        postReaction.setDislikeCount(0L);
        when(postReactionRepository.findByPost_Id(postId)).thenReturn(java.util.Optional.of(postReaction));
        assertEquals(1L, postReaction.getLikeCount());
        assertEquals(0L, postReaction.getDislikeCount());
    }

    @Test
    void test_dislikePost_WhenActiveEntryExistsAndIsLiked() {
        Long postId = 1L;
        Long userId = 1L;
        PostUserInteraction postUserInteraction = new PostUserInteraction();
        postUserInteraction.setIsLiked(true);
        postUserInteraction.setIsActive(true);
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.of(postUserInteraction));
        PostReaction postReaction = new PostReaction();
        postReaction.setLikeCount(1L);
        postReaction.setDislikeCount(0L);
        when(postReactionRepository.findByPost_Id(postId)).thenReturn(java.util.Optional.of(postReaction));
        postUserInteractionService.dislikePost(postId, userId);
        assertEquals(0L, postReaction.getLikeCount());
        assertEquals(1L, postReaction.getDislikeCount());
    }

    @Test
    void test_dislikePost_WhenActiveEntryDoesNotExist() {
        Long postId = 1L;
        Long userId = 1L;
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.empty());
        Post post = new Post();
        post.setId(postId);
        when(postRepository.findById(postId)).thenReturn(java.util.Optional.of(post));
        EndUser endUser = new EndUser();
        endUser.setId(userId);
        when(endUserRepository.findById(userId)).thenReturn(java.util.Optional.of(endUser));
        postUserInteractionService.dislikePost(postId, userId);
        PostUserInteraction postUserInteraction = new PostUserInteraction();
        postUserInteraction.setPost(post);
        postUserInteraction.setIsLiked(false);
        postUserInteraction.setEndUser(endUser);
        postUserInteraction.setIsActive(true);
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.of(postUserInteraction));
        PostReaction postReaction = new PostReaction();
        postReaction.setPost(post);
        postReaction.setLikeCount(0L);
        postReaction.setDislikeCount(1L);
        when(postReactionRepository.findByPost_Id(postId)).thenReturn(java.util.Optional.of(postReaction));
        assertEquals(0L, postReaction.getLikeCount());
        assertEquals(1L, postReaction.getDislikeCount());
    }

    @Test
    void test_dislikePost_WhenAlreadyDisliked() {
        Long postId = 1L;
        Long userId = 1L;
        PostUserInteraction postUserInteraction = new PostUserInteraction();
        postUserInteraction.setIsLiked(false);
        postUserInteraction.setIsActive(true);
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.of(postUserInteraction));
        when(postRepository.findById(postId)).thenReturn(java.util.Optional.of(new Post()));
        when(endUserRepository.findById(userId)).thenReturn(java.util.Optional.of(new EndUser()));
        postUserInteractionService.dislikePost(postId, userId);
        PostReaction postReaction = new PostReaction();
        postReaction.setLikeCount(0L);
        postReaction.setDislikeCount(1L);
        when(postReactionRepository.findByPost_Id(postId)).thenReturn(java.util.Optional.of(postReaction));
        assertEquals(0L, postReaction.getLikeCount());
        assertEquals(1L, postReaction.getDislikeCount());
    }

    @Test
    void test_getUserWhoLikedPostById_Success() {
        Long postId = 1L;
        Integer pageNo = 0;
        Integer pageSize = 10;
        Page<EndUserProjection> endUserProjectionList = Page.empty();
        when(postUserInteractionRepository.getUserLikeInteractionByPostId(postId, PageRequest.of(pageNo, pageSize))).thenReturn(endUserProjectionList);
        GetEndUsersResponse response = postUserInteractionService.getUserWhoLikedPostById(postId, pageNo, pageSize);
        assertNotNull(response);
        assertEquals(endUserProjectionList, response.getEndUserList());
    }

    @Test
    void test_getUserWhoLikedPostById_WithNonEmptyList() {
        Long postId = 1L;
        Integer pageNo = 0;
        Integer pageSize = 10;
        Page<EndUserProjection> page = getUserProjections();
        when(postUserInteractionRepository.getUserLikeInteractionByPostId(postId, PageRequest.of(pageNo, pageSize))).thenReturn(page);
        GetEndUsersResponse response = postUserInteractionService.getUserWhoLikedPostById(postId, pageNo, pageSize);
        assertNotNull(response);
        assertEquals(page, response.getEndUserList());
    }

    @Test
    void test_getUserWhoDislikedPostById_Success() {
        Long postId = 1L;
        Integer pageNo = 0;
        Integer pageSize = 10;
        Page<EndUserProjection> endUserProjectionList = Page.empty();
        when(postUserInteractionRepository.getUserDislikeInteractionByPostId(postId, PageRequest.of(pageNo, pageSize))).thenReturn(endUserProjectionList);
        GetEndUsersResponse response = postUserInteractionService.getUserWhoDislikedPostById(postId, pageNo, pageSize);
        assertNotNull(response);
        assertEquals(endUserProjectionList, response.getEndUserList());
    }

    @Test
    void test_getUserWhoDislikedPostById_WithNonEmptyList() {
        Long postId = 1L;
        Integer pageNo = 0;
        Integer pageSize = 10;
        Page<EndUserProjection> page = getUserProjections();
        when(postUserInteractionRepository.getUserDislikeInteractionByPostId(postId, PageRequest.of(pageNo, pageSize))).thenReturn(page);
        GetEndUsersResponse response = postUserInteractionService.getUserWhoDislikedPostById(postId, pageNo, pageSize);
        assertNotNull(response);
        assertEquals(page, response.getEndUserList());
    }

    private static Page<EndUserProjection> getUserProjections() {
        List<EndUserProjection> content = List.of(new EndUserProjection() {
            @Override
            public String getId() {
                return "1";
            }

            @Override
            public String getName() {
                return "Jane Doe";
            }

            @Override
            public String getEmail() {
                return "jane@example.com";
            }
        });
        return new PageImpl<>(content, PageRequest.of(0, 10), 10);
    }

    @Test
    void test_removeLikePost_WhenActiveEntryExistsAndIsLiked() {
        long postId = 1L;
        long userId = 1L;
        PostUserInteraction postUserInteraction = new PostUserInteraction();
        postUserInteraction.setIsLiked(true);
        postUserInteraction.setIsActive(true);
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.of(postUserInteraction));
        PostReaction postReaction = new PostReaction();
        postReaction.setLikeCount(1L);
        postReaction.setDislikeCount(0L);
        when(postReactionRepository.findByPost_Id(postId)).thenReturn(java.util.Optional.of(postReaction));
        postUserInteractionService.removeLikePost(postId, userId);
        assertEquals(0L, postReaction.getLikeCount());
    }

    @Test
    void test_removeLikePost_WhenActiveEntryDoesNotExist() {
        long postId = 1L;
        long userId = 1L;
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.empty());
        postUserInteractionService.removeLikePost(postId, userId);
    }

    @Test
    void test_removeLikePost_WhenNotLiked() {
        long postId = 1L;
        long userId = 1L;
        PostUserInteraction postUserInteraction = new PostUserInteraction();
        postUserInteraction.setIsLiked(false);
        postUserInteraction.setIsActive(true);
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.of(postUserInteraction));
        postUserInteractionService.removeLikePost(postId, userId);
    }

    @Test
    void test_removeDislikePost_WhenActiveEntryExistsAndIsLiked() {
        long postId = 1L;
        long userId = 1L;
        PostUserInteraction postUserInteraction = new PostUserInteraction();
        postUserInteraction.setIsLiked(true);
        postUserInteraction.setIsActive(true);
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.of(postUserInteraction));
        PostReaction postReaction = new PostReaction();
        postReaction.setLikeCount(1L);
        postReaction.setDislikeCount(0L);
        when(postReactionRepository.findByPost_Id(postId)).thenReturn(java.util.Optional.of(postReaction));
        postUserInteractionService.removeDislikePost(postId, userId);
        assertEquals(0L, postReaction.getDislikeCount());
    }

    @Test
    void test_removeDislikePost_WhenActiveEntryDoesNotExist() {
        long postId = 1L;
        long userId = 1L;
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.empty());
        postUserInteractionService.removeDislikePost(postId, userId);
    }

    @Test
    void test_removeDislikePost_WhenNotDisliked() {
        long postId = 1L;
        long userId = 1L;
        PostUserInteraction postUserInteraction = new PostUserInteraction();
        postUserInteraction.setIsLiked(false);
        postUserInteraction.setIsActive(true);
        when(postUserInteractionRepository.findByEndUser_IdAndIsActiveAndPost_Id(userId, true, postId)).thenReturn(java.util.Optional.of(postUserInteraction));
        postUserInteractionService.removeDislikePost(postId, userId);
    }
}
