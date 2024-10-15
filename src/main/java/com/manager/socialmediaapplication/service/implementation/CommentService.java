package com.manager.socialmediaapplication.service.implementation;

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
import com.manager.socialmediaapplication.service.intrface.CommentServiceInterface;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService implements CommentServiceInterface {
    CommentRepository commentRepository;
    PostRepository postRepository;
    EndUserRepository endUserRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, EndUserRepository endUserRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.endUserRepository = endUserRepository;
    }


    @Override
    public GetCommentResponse createComment(CommentCreationRequest request) {
        log.info("Creating a new comment for post ID: {} by user ID: {}", request.getPostId(), request.getUserId());
        Post post = postRepository.findById(request.getPostId()).get();
        EndUser endUser = endUserRepository.findById(request.getUserId()).get();
        Comment comment = new Comment();
        comment.setEndUser(endUser);
        comment.setPost(post);
        comment.setContent(request.getContent());
        if (request.getParentId() != null) {
            comment.setParent(commentRepository.findById(request.getParentId()).orElse(null));
            log.info("Setting parent comment ID: {}", request.getParentId());
        }
        Comment newComment = commentRepository.save(comment);
        log.info("Comment created successfully with ID: {}", newComment.getId());
        GetCommentResponse response = new GetCommentResponse(commentRepository.findCommentByCommentId(newComment.getId()));
        return response;
    }

    @Override
    public GetCommentsResponse getCommentsForPost(Long postId, Integer pageSize, Integer pageNumber, String order) {
        log.info("Fetching comments for post ID: {} with page size: {}, page number: {}, order: {}", postId, pageSize, pageNumber, order);
        GetCommentsResponse response = new GetCommentsResponse();
        Sort sort = "DESC".equalsIgnoreCase(order) ? Sort.by("CREATED_DATE").descending() : Sort.by("CREATED_DATE").ascending();
        Page<CommentProjection> commentProjections = commentRepository.getCommentsByPost(postId, PageRequest.of(pageNumber, pageSize, sort));
        log.info("Fetched {} comments for post ID: {}", commentProjections.getTotalElements(), postId);
        response.setResponse(commentProjections);
        return response;
    }

    @Override
    public GetCommentsResponse getCommentsForParent(Long parentCommentId, Integer pageSize, Integer pageNumber, String order) {
        log.info("Fetching comments for parent comment ID: {} with page size: {}, page number: {}, order: {}", parentCommentId, pageSize, pageNumber, order);
        GetCommentsResponse response = new GetCommentsResponse();
        Sort sort = "DESC".equalsIgnoreCase(order) ? Sort.by("CREATED_DATE").descending() : Sort.by("CREATED_DATE").ascending();
        Page<CommentProjection> commentProjections = commentRepository.findCommentsByCommentParentId(parentCommentId , PageRequest.of(pageNumber, pageSize, sort));
        log.info("Fetched {} comments for parent comment ID: {}", commentProjections.getTotalElements(), parentCommentId);
        response.setResponse(commentProjections);
        return response;
    }

    @Override
    public GetCommentResponse updateComment(Long commentId, UpdateCommentRequest request) {
        log.info("Updating comment ID: {} with new content", commentId);
        commentRepository.updateCommentById(commentId, request.getUpdatedContent());
        log.info("Successfully updated comment ID: {}", commentId);
        GetCommentResponse response = new GetCommentResponse();
        CommentProjection commentProjection = commentRepository.findCommentByCommentId(commentId);
        response.setCommentProjection(commentProjection);
        return response;
    }

    public Comment getCommentById(Long commentId) {
        log.debug("Retrieving full details for comment ID: {}", commentId);
        return commentRepository.findById(commentId).get();
    }

    public CommentProjection getCommentProjectionById(Long commentId) {
        log.debug("Retrieving projection for comment ID: {}", commentId);
        return commentRepository.findCommentByCommentId(commentId);
    }
}
