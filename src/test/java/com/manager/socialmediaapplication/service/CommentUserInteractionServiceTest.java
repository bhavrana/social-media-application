package com.manager.socialmediaapplication.service;

import com.manager.socialmediaapplication.dto.response.GetCommentResponse;
import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;
import com.manager.socialmediaapplication.model.Comment;
import com.manager.socialmediaapplication.model.CommentReaction;
import com.manager.socialmediaapplication.model.CommentUserInteraction;
import com.manager.socialmediaapplication.model.EndUser;
import com.manager.socialmediaapplication.model.projection.CommentProjection;
import com.manager.socialmediaapplication.model.projection.EndUserProjection;
import com.manager.socialmediaapplication.repository.CommentReactionRepository;
import com.manager.socialmediaapplication.repository.CommentUserInteractionRepository;
import com.manager.socialmediaapplication.repository.EndUserRepository;
import com.manager.socialmediaapplication.service.implementation.CommentService;
import com.manager.socialmediaapplication.service.implementation.CommentUserInteractionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CommentUserInteractionServiceTest {
    @InjectMocks
    CommentUserInteractionService commentUserInteractionService;

    @Mock
    CommentUserInteractionRepository commentUserInteractionRepository;

    @Mock
    CommentReactionRepository commentReactionRepository;

    @Mock
    CommentService commentService;

    @Mock
    EndUserRepository endUserRepository;

    @Test
    void test_likeComment_WhenActiveEntryExists() {
        Long commentId = 1L;
        Long userId = 1L;
        CommentUserInteraction commentUserInteraction = new CommentUserInteraction();
        commentUserInteraction.setIsLiked(false);
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.of(commentUserInteraction));
        CommentReaction commentReaction = new CommentReaction();
        commentReaction.setLikeCount(0L);
        commentReaction.setDislikeCount(1L);
        when(commentReactionRepository.findByComment_Id(commentId)).thenReturn(java.util.Optional.of(commentReaction));
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
                return "Test user";
            }

            @Override
            public Long getLikeCount() {
                return 1L;
            }

            @Override
            public Long getDislikeCount() {
                return 0L;
            }
        };
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);

        GetCommentResponse response = commentUserInteractionService.likeComment(commentId, userId);

        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(1L, response.getCommentProjection().getLikeCount());
        assertEquals(0L, response.getCommentProjection().getDislikeCount());
    }

    @Test
    void test_likeComment_WhenActiveEntryDoesNotExist() {
        Long commentId = 1L;
        Long userId = 1L;
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.empty());
        Comment comment = new Comment();
        comment.setId(commentId);
        when(commentService.getCommentById(commentId)).thenReturn(comment);
        EndUser endUser = new EndUser();
        endUser.setId(userId);
        when(endUserRepository.findById(userId)).thenReturn(java.util.Optional.of(endUser));
        when(commentReactionRepository.findByComment_Id(commentId)).thenReturn(java.util.Optional.empty());
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
                return "Test user";
            }

            @Override
            public Long getLikeCount() {
                return 1L;
            }

            @Override
            public Long getDislikeCount() {
                return 0L;
            }
        };
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);

        GetCommentResponse response = commentUserInteractionService.likeComment(commentId, userId);

        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(1L, response.getCommentProjection().getLikeCount());
        assertEquals(0L, response.getCommentProjection().getDislikeCount());
    }

    @Test
    void test_likeComment_WhenAlreadyLiked() {
        Long commentId = 1L;
        Long userId = 1L;
        CommentUserInteraction commentUserInteraction = new CommentUserInteraction();
        commentUserInteraction.setIsLiked(true);
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.of(commentUserInteraction));
        CommentReaction commentReaction = new CommentReaction();
        when(commentReactionRepository.findByComment_Id(commentId)).thenReturn(java.util.Optional.of(commentReaction));

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
                return "Test user";
            }

            @Override
            public Long getLikeCount() {
                return 1L;
            }

            @Override
            public Long getDislikeCount() {
                return 0L;
            }
        };
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);

        GetCommentResponse response = commentUserInteractionService.likeComment(commentId, userId);

        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(1L, response.getCommentProjection().getLikeCount());
        assertEquals(0L, response.getCommentProjection().getDislikeCount());
    }

    @Test
    void test_dislikeComment_WhenActiveEntryExistsAndIsLiked() {
        Long commentId = 1L;
        Long userId = 1L;
        CommentUserInteraction commentUserInteraction = new CommentUserInteraction();
        commentUserInteraction.setIsLiked(true);
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.of(commentUserInteraction));
        CommentReaction commentReaction = new CommentReaction();
        commentReaction.setLikeCount(1L);
        commentReaction.setDislikeCount(0L);
        when(commentReactionRepository.findByComment_Id(commentId)).thenReturn(java.util.Optional.of(commentReaction));
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
                return "Test user";
            }

            @Override
            public Long getLikeCount() {
                return 0L;
            }

            @Override
            public Long getDislikeCount() {
                return 1L;
            }
        };
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);

        GetCommentResponse response = commentUserInteractionService.dislikeComment(commentId, userId);

        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(0L, response.getCommentProjection().getLikeCount());
        assertEquals(1L, response.getCommentProjection().getDislikeCount());
    }

    @Test
    void test_dislikeComment_WhenActiveEntryDoesNotExist() {
        Long commentId = 1L;
        Long userId = 1L;
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.empty());
        Comment comment = new Comment();
        comment.setId(commentId);
        when(commentService.getCommentById(commentId)).thenReturn(comment);
        EndUser endUser = new EndUser();
        endUser.setId(userId);
        when(endUserRepository.findById(userId)).thenReturn(java.util.Optional.of(endUser));
        when(commentReactionRepository.findByComment_Id(commentId)).thenReturn(java.util.Optional.empty());
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
                return "Test user";
            }

            @Override
            public Long getLikeCount() {
                return 0L;
            }

            @Override
            public Long getDislikeCount() {
                return 1L;
            }
        };
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);

        GetCommentResponse response = commentUserInteractionService.dislikeComment(commentId, userId);

        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(0L, response.getCommentProjection().getLikeCount());
        assertEquals(1L, response.getCommentProjection().getDislikeCount());
    }

    @Test
    void test_dislikeComment_WhenAlreadyDisliked() {
        Long commentId = 1L;
        Long userId = 1L;
        CommentUserInteraction commentUserInteraction = new CommentUserInteraction();
        commentUserInteraction.setIsLiked(false);
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.of(commentUserInteraction));
        when(endUserRepository.findById(userId)).thenReturn(java.util.Optional.of(new EndUser()));
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
                return "Test user";
            }

            @Override
            public Long getLikeCount() {
                return 0L;
            }

            @Override
            public Long getDislikeCount() {
                return 1L;
            }
        };
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);

        GetCommentResponse response = commentUserInteractionService.dislikeComment(commentId, userId);

        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(0L, response.getCommentProjection().getLikeCount());
        assertEquals(1L, response.getCommentProjection().getDislikeCount());
    }

    @Test
    void test_getUserWhoLikedCommentById() {
        Long commentId = 1L;
        Integer pageNo = 0;
        Integer pageSize = 10;
        Page<EndUserProjection> endUserProjectionList = Page.empty();
        when(commentUserInteractionRepository.getUserLikeInteractionByCommentId(commentId, PageRequest.of(pageNo, pageSize))).thenReturn(endUserProjectionList);

        GetEndUsersResponse response = commentUserInteractionService.getUserWhoLikedCommentById(commentId, pageNo, pageSize);

        assertNotNull(response);
        assertEquals(endUserProjectionList, response.getEndUserList());
    }

    @Test
    void test_getUserWhoLikedCommentById_WithNonEmptyList() {
        Long commentId = 1L;
        Integer pageNo = 0;
        Integer pageSize = 10;
        Page<EndUserProjection> page = getEndUserProjections();
        when(commentUserInteractionRepository.getUserLikeInteractionByCommentId(commentId, PageRequest.of(pageNo, pageSize))).thenReturn(page);
        GetEndUsersResponse response = commentUserInteractionService.getUserWhoLikedCommentById(commentId, pageNo, pageSize);
        assertNotNull(response);
        assertEquals(page, response.getEndUserList());
    }

    private static Page<EndUserProjection> getEndUserProjections() {
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

        Page<EndUserProjection> page = new PageImpl<>(content, PageRequest.of(0, 10), 10);
        return page;
    }

    @Test
    void test_getUserWhoDislikedCommentById() {
        Long commentId = 1L;
        Integer pageNo = 0;
        Integer pageSize = 10;
        Page<EndUserProjection> endUserProjectionList = Page.empty();
        when(commentUserInteractionRepository.getUserDislikeInteractionByCommentId(commentId, PageRequest.of(pageNo, pageSize))).thenReturn(endUserProjectionList);

        GetEndUsersResponse response = commentUserInteractionService.getUserWhoDislikedCommentById(commentId, pageNo, pageSize);

        assertNotNull(response);
        assertEquals(endUserProjectionList, response.getEndUserList());
    }

    @Test
    void test_getUserWhoDislikedCommentById_WithNonEmptyList() {
        Long commentId = 1L;
        Integer pageNo = 0;
        Integer pageSize = 10;
        Page<EndUserProjection> page = getEndUserProjections();
        when(commentUserInteractionRepository.getUserDislikeInteractionByCommentId(commentId, PageRequest.of(pageNo, pageSize))).thenReturn(page);

        GetEndUsersResponse response = commentUserInteractionService.getUserWhoDislikedCommentById(commentId, pageNo, pageSize);

        assertNotNull(response);
        assertEquals(page, response.getEndUserList());
    }

    @Test
    void test_removeLikeComment_WhenActiveEntryExistsAndIsLiked() {
        long commentId = 1L;
        long userId = 1L;
        CommentUserInteraction commentUserInteraction = new CommentUserInteraction();
        commentUserInteraction.setIsLiked(true);
        commentUserInteraction.setIsActive(true);
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.of(commentUserInteraction));
        CommentReaction commentReaction = new CommentReaction();
        commentReaction.setLikeCount(1L);
        commentReaction.setDislikeCount(0L);
        when(commentReactionRepository.findByComment_Id(commentId)).thenReturn(java.util.Optional.of(commentReaction));
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
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);

        GetCommentResponse response = commentUserInteractionService.removeLikeComment(commentId, userId);

        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(0L, response.getCommentProjection().getLikeCount());
        assertEquals(0L, response.getCommentProjection().getDislikeCount());
    }

    @Test
    void test_removeLikeComment_WhenActiveEntryDoesNotExist() {
        long commentId = 1L;
        long userId = 1L;
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.empty());
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
                return "Test user";
            }

            @Override
            public Long getLikeCount() {
                return 1L;
            }

            @Override
            public Long getDislikeCount() {
                return 0L;
            }
        };
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);

        GetCommentResponse response = commentUserInteractionService.removeLikeComment(commentId, userId);

        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(1L, response.getCommentProjection().getLikeCount());
        assertEquals(0L, response.getCommentProjection().getDislikeCount());
    }

    @Test
    void test_removeLikeComment_WhenNotLiked() {
        long commentId = 1L;
        long userId = 1L;
        CommentUserInteraction commentUserInteraction = new CommentUserInteraction();
        commentUserInteraction.setIsLiked(false);
        commentUserInteraction.setIsActive(true);
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.of(commentUserInteraction));
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
                return "Test user";
            }

            @Override
            public Long getLikeCount() {
                return 1L;
            }

            @Override
            public Long getDislikeCount() {
                return 0L;
            }
        };
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);

        GetCommentResponse response = commentUserInteractionService.removeLikeComment(commentId, userId);

        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(1L, response.getCommentProjection().getLikeCount());
        assertEquals(0L, response.getCommentProjection().getDislikeCount());
    }

    @Test
    void test_removeLikeComment_WhenLikeCountBecomesZero() {
        long commentId = 1L;
        long userId = 1L;
        CommentUserInteraction commentUserInteraction = new CommentUserInteraction();
        commentUserInteraction.setIsLiked(true);
        commentUserInteraction.setIsActive(true);
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.of(commentUserInteraction));
        CommentReaction commentReaction = new CommentReaction();
        commentReaction.setLikeCount(1L);
        commentReaction.setDislikeCount(0L);
        when(commentReactionRepository.findByComment_Id(commentId)).thenReturn(java.util.Optional.of(commentReaction));
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
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);

        GetCommentResponse response = commentUserInteractionService.removeLikeComment(commentId, userId);

        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(0L, response.getCommentProjection().getLikeCount());
        assertEquals(0L, response.getCommentProjection().getDislikeCount());
    }

    @Test
    void test_removeDislikeComment_WhenActiveEntryExistsAndIsDisliked() {
        long commentId = 1L;
        long userId = 1L;
        CommentUserInteraction commentUserInteraction = new CommentUserInteraction();
        commentUserInteraction.setIsLiked(false);
        commentUserInteraction.setIsActive(true);
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.of(commentUserInteraction));
        CommentReaction commentReaction = new CommentReaction();
        commentReaction.setLikeCount(0L);
        commentReaction.setDislikeCount(1L);
        when(commentReactionRepository.findByComment_Id(commentId)).thenReturn(java.util.Optional.of(commentReaction));
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
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);
        GetCommentResponse response = commentUserInteractionService.removeDislikeComment(commentId, userId);
        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(0L, response.getCommentProjection().getLikeCount());
        assertEquals(0L, response.getCommentProjection().getDislikeCount());
    }

    @Test
    void test_removeDislikeComment_WhenActiveEntryDoesNotExist() {
        long commentId = 1L;
        long userId = 1L;
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.empty());
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
                return "Test user";
            }

            @Override
            public Long getLikeCount() {
                return 0L;
            }

            @Override
            public Long getDislikeCount() {
                return 1L;
            }
        };
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);

        GetCommentResponse response = commentUserInteractionService.removeDislikeComment(commentId, userId);

        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(0L, response.getCommentProjection().getLikeCount());
        assertEquals(1L, response.getCommentProjection().getDislikeCount());
    }

    @Test
    void test_removeDislikeComment_WhenNotDisliked() {
        long commentId = 1L;
        long userId = 1L;
        CommentUserInteraction commentUserInteraction = new CommentUserInteraction();
        commentUserInteraction.setIsLiked(true);
        commentUserInteraction.setIsActive(true);
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.of(commentUserInteraction));
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
                return "Test user";
            }

            @Override
            public Long getLikeCount() {
                return 1L;
            }

            @Override
            public Long getDislikeCount() {
                return 0L;
            }
        };
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);

        GetCommentResponse response = commentUserInteractionService.removeDislikeComment(commentId, userId);

        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(1L, response.getCommentProjection().getLikeCount());
        assertEquals(0L, response.getCommentProjection().getDislikeCount());
    }

    @Test
    void test_removeDislikeComment_WhenDislikeCountBecomesZero() {
        long commentId = 1L;
        long userId = 1L;
        CommentUserInteraction commentUserInteraction = new CommentUserInteraction();
        commentUserInteraction.setIsLiked(false);
        commentUserInteraction.setIsActive(true);
        when(commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true, commentId)).thenReturn(java.util.Optional.of(commentUserInteraction));
        CommentReaction commentReaction = new CommentReaction();
        commentReaction.setLikeCount(0L);
        commentReaction.setDislikeCount(1L);
        when(commentReactionRepository.findByComment_Id(commentId)).thenReturn(java.util.Optional.of(commentReaction));
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
        when(commentService.getCommentProjectionById(commentId)).thenReturn(commentProjection);

        GetCommentResponse response = commentUserInteractionService.removeDislikeComment(commentId, userId);

        assertNotNull(response);
        assertEquals(commentId, response.getCommentProjection().getId());
        assertEquals(0L, response.getCommentProjection().getLikeCount());
        assertEquals(0L, response.getCommentProjection().getDislikeCount());
    }
}
