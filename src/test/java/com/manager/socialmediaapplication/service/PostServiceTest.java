package com.manager.socialmediaapplication.service;

import com.manager.socialmediaapplication.dto.request.PostCreationRequest;
import com.manager.socialmediaapplication.dto.request.UpdatePostRequest;
import com.manager.socialmediaapplication.dto.response.GetPostResponse;
import com.manager.socialmediaapplication.dto.response.GetPostsResponse;
import com.manager.socialmediaapplication.model.EndUser;
import com.manager.socialmediaapplication.model.Post;
import com.manager.socialmediaapplication.model.projection.PostProjection;
import com.manager.socialmediaapplication.repository.PostRepository;
import com.manager.socialmediaapplication.service.implementation.EndUserService;
import com.manager.socialmediaapplication.service.implementation.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {
    @InjectMocks
    PostService postService;

    @Mock
    EndUserService endUserService;

    @Mock
    PostRepository postRepository;

    @Test
    void testCreatePostSuccess() {
        PostCreationRequest request = new PostCreationRequest();
        request.setUserId(1L);
        request.setContent("Test content");

        EndUser endUser = new EndUser();
        endUser.setId(request.getUserId());
        when(endUserService.getEndUserById(request.getUserId())).thenReturn(endUser);

        Post post = new Post();
        post.setContent(request.getContent());
        post.setEndUser(endUser);
        post.setId(1L);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostProjection postProjection = new PostProjection() {
            @Override
            public Long getId() {
                return 1L;
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
                return "Test user";
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
        when(postRepository.findPostByPostId(post.getId())).thenReturn(postProjection);
        GetPostResponse response = postService.createPost(request);
        assertNotNull(response);
        assertEquals(postProjection, response.getPostProjection());
    }

    @Test
    void test_getPosts_Ascending() {
        Integer pageSize = 10;
        Integer pageNo = 0;
        String order = "ASC";
        Sort sort = Sort.by("CREATED_DATE").ascending();
        Page<PostProjection> postResponses = Page.empty();
        when(postRepository.getPosts(PageRequest.of(pageNo, pageSize, sort))).thenReturn(postResponses);

        GetPostsResponse response = postService.getPosts(pageSize, pageNo, order);

        assertNotNull(response);
        assertEquals(postResponses, response.getResponse());
    }

    @Test
    void test_getPosts_Descending() {
        Integer pageSize = 10;
        Integer pageNo = 0;
        String order = "DESC";
        Sort sort = Sort.by("CREATED_DATE").descending();
        Page<PostProjection> postResponses = Page.empty();
        when(postRepository.getPosts(PageRequest.of(pageNo, pageSize, sort))).thenReturn(postResponses);

        GetPostsResponse response = postService.getPosts(pageSize, pageNo, order);

        assertNotNull(response);
        assertEquals(postResponses, response.getResponse());
    }

    @Test
    void test_getPost_Success() {
        Long postId = 1L;
        PostProjection postProjection = new PostProjection() {
            @Override
            public Long getId() {
                return postId;
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
                return "Test user";
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
        when(postRepository.findPostByPostId(postId)).thenReturn(postProjection);

        GetPostResponse response = postService.getPost(postId);

        assertNotNull(response);
        assertEquals(postProjection, response.getPostProjection());
    }

    @Test
    void test_updatePost_Success() {
        long postId = 1L;
        UpdatePostRequest request = new UpdatePostRequest();
        request.setUpdatedContent("Updated content");
        PostProjection postProjection = new PostProjection() {
            @Override
            public Long getId() {
                return postId;
            }

            @Override
            public String getContent() {
                return request.getUpdatedContent();
            }

            @Override
            public LocalDateTime getCreatedDate() {
                return LocalDateTime.now();
            }

            @Override
            public String getEndUserName() {
                return "Test user";
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
        when(postRepository.findPostByPostId(postId)).thenReturn(postProjection);
        GetPostResponse response = postService.updatePost(postId, request);
        assertNotNull(response);
        assertEquals(postProjection, response.getPostProjection());
    }

    @Test
    void test_getPostForUserId_Ascending() {
        long userId = 1L;
        Integer pageSize = 10;
        Integer pageNo = 0;
        String order = "ASC";
        Sort sort = Sort.by("CREATED_DATE").ascending();
        Page<PostProjection> postResponses = Page.empty();
        when(postRepository.getPostForUserId(userId, PageRequest.of(pageNo, pageSize, sort))).thenReturn(postResponses);
        GetPostsResponse response = postService.getPostForUserId(userId, pageSize, pageNo, order);
        assertNotNull(response);
        assertEquals(postResponses, response.getResponse());
    }

    @Test
    void test_getPostForUserId_Descending() {
        long userId = 1L;
        Integer pageSize = 10;
        Integer pageNo = 0;
        String order = "DESC";
        Sort sort = Sort.by("CREATED_DATE").descending();
        Page<PostProjection> postResponses = Page.empty();
        when(postRepository.getPostForUserId(userId, PageRequest.of(pageNo, pageSize, sort))).thenReturn(postResponses);
        GetPostsResponse response = postService.getPostForUserId(userId, pageSize, pageNo, order);
        assertNotNull(response);
        assertEquals(postResponses, response.getResponse());
    }
}
