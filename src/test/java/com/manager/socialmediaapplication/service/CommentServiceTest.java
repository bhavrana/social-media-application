package com.manager.socialmediaapplication.service;

import com.manager.socialmediaapplication.dto.request.CommentCreationRequest;
import com.manager.socialmediaapplication.dto.request.UpdateCommentRequest;
import com.manager.socialmediaapplication.dto.response.GetCommentResponse;
import com.manager.socialmediaapplication.dto.response.GetCommentsResponse;
import com.manager.socialmediaapplication.model.Comment;
import com.manager.socialmediaapplication.model.EndUser;
import com.manager.socialmediaapplication.model.Post;
import com.manager.socialmediaapplication.model.projection.CommentProjection;
import com.manager.socialmediaapplication.repository.CommentRepository;
import com.manager.socialmediaapplication.repository.EndUserRepository;
import com.manager.socialmediaapplication.repository.PostRepository;
import com.manager.socialmediaapplication.service.implementation.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private EndUserRepository endUserRepository;


    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createCommentWithoutParentTest() {
        CommentCreationRequest request = new CommentCreationRequest("Content", 1L, 1L, null);
        Post post = new Post();
        EndUser endUser = new EndUser();
        when(postRepository.findById(request.getPostId())).thenReturn(Optional.of(post));
        when(endUserRepository.findById(request.getUserId())).thenReturn(Optional.of(endUser));
        commentService.createComment(request);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createCommentWithParentTest() {
        CommentCreationRequest request = new CommentCreationRequest("Content", 1L, 1L, 1L);
        Post post = new Post();
        EndUser endUser = new EndUser();
        when(postRepository.findById(request.getPostId())).thenReturn(Optional.of(post));
        when(endUserRepository.findById(request.getUserId())).thenReturn(Optional.of(endUser));
        when(commentRepository.findById(request.getParentId())).thenReturn(Optional.of(new Comment()));
        commentService.createComment(request);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void getCommentsForPost_NoCommentInPost_Test() {
        when(commentRepository
                .getCommentsByPost(1L, PageRequest.of(0, 3, Sort.by("CREATED_DATE").descending()))).thenReturn(Page.empty());
        GetCommentsResponse response = commentService.getCommentsForPost(1L, 3,0,"DESC");
        assertThat(response.getResponse()).isEmpty();
    }

    @Test
    void getCommentsForPost_CommentSortedInDesc_Test() {
        Long postId = 1L;
        Integer pageSize = 10;
        Integer pageNumber = 0;
        String order = "DESC";
        CommentProjection[] commentProjection = new CommentProjection[3];
        for(int i = 0; i < 3; ++i) {
            int finalI = i;
            commentProjection[i] = new CommentProjection() {
                @Override
                public Long getId() {
                    return (long) finalI;
                }

                @Override
                public String getContent() {
                    return "Content" + finalI;
                }

                @Override
                public LocalDateTime getCreatedDate() {
                    return LocalDateTime.now();
                }

                @Override
                public String getEndUserName() {
                    return "End User" + finalI;
                }

                @Override
                public Long getLikeCount() {
                    return (long) finalI;
                }

                @Override
                public Long getDislikeCount() {
                    return (long) (finalI * 3);
                }
            };
        }
        Page<CommentProjection> commentProjections = new PageImpl<>(List.of(commentProjection));
        when(commentRepository.getCommentsByPost(anyLong(), any(PageRequest.class))).thenReturn(commentProjections);
        GetCommentsResponse response = commentService.getCommentsForPost(postId, pageSize, pageNumber, order);
        assertNotNull(response);
        assertEquals(commentProjections, response.getResponse());
    }

    @Test
    void getCommentsForPost_CommentSortedInAsc_Test() {
        Long postId = 1L;
        Integer pageSize = 10;
        Integer pageNumber = 0;
        String order = "ASC";
        CommentProjection[] commentProjection = new CommentProjection[3];
        for(int i = 0; i < 3; ++i) {
            int finalI = i;
            commentProjection[i] = new CommentProjection() {
                @Override
                public Long getId() {
                    return (long) finalI;
                }

                @Override
                public String getContent() {
                    return "Content" + finalI;
                }

                @Override
                public LocalDateTime getCreatedDate() {
                    return LocalDateTime.now();
                }

                @Override
                public String getEndUserName() {
                    return "End User" + finalI;
                }

                @Override
                public Long getLikeCount() {
                    return (long) finalI;
                }

                @Override
                public Long getDislikeCount() {
                    return (long) (finalI * 3);
                }
            };
        }
        Page<CommentProjection> commentProjections = new PageImpl<>(List.of(commentProjection));
        when(commentRepository.getCommentsByPost(anyLong(), any(PageRequest.class))).thenReturn(commentProjections);
        GetCommentsResponse response = commentService.getCommentsForPost(postId, pageSize, pageNumber, order);
        assertNotNull(response);
        assertEquals(commentProjections, response.getResponse());
    }

    @Test
    void testUpdateComment() {
        Long commentId = 1L;
        UpdateCommentRequest request = new UpdateCommentRequest("Updated content");
        CommentProjection commentProjection = new CommentProjection() {
            @Override
            public Long getId() {
                return commentId;
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

        when(commentRepository.findCommentByCommentId(anyLong())).thenReturn(commentProjection);
        GetCommentResponse response = commentService.updateComment(commentId, request);
        assertNotNull(response);
        assertEquals(commentProjection, response.getCommentProjection());
    }

    @Test
    void testGetCommentById() {
        Long commentId = 1L;
        Comment comment = new Comment();
        comment.setId(commentId);
        when(commentRepository.findById(anyLong())).thenReturn(java.util.Optional.of(comment));
        Comment response = commentService.getCommentById(commentId);
        assertNotNull(response);
        assertEquals(commentId, response.getId());
    }

    @Test
    void testGetCommentProjectionById() {
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
        when(commentRepository.findCommentByCommentId(anyLong())).thenReturn(commentProjection);
        CommentProjection response = commentService.getCommentProjectionById(commentId);
        assertNotNull(response);
        assertEquals(commentId, response.getId());
    }
}
