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
    public void createComment(CommentCreationRequest request) {
        Post post = postRepository.findById(request.getPostId()).get();
        EndUser endUser = endUserRepository.findById(request.getUserId()).get();
        Comment comment = new Comment();
        comment.setEndUser(endUser);
        comment.setPost(post);
        comment.setContent(request.getContent());
        comment.setParent(request.getParentId() == null ? null : commentRepository.findById(request.getParentId()).get());
        commentRepository.save(comment);
    }

    @Override
    public GetCommentsResponse getCommentsForPost(Long postId, Integer pageSize, Integer pageNumber, String order) {
        GetCommentsResponse response = new GetCommentsResponse();
        Sort sort = "DESC".equalsIgnoreCase(order) ? Sort.by("CREATED_DATE").descending() : Sort.by("CREATED_DATE").ascending();
        Page<CommentProjection> commentProjections = commentRepository.getCommentsByPost(postId, PageRequest.of(pageNumber, pageSize, sort));
        response.setResponse(commentProjections);
        return response;
    }

    @Override
    public GetCommentsResponse getCommentsForParent(Long parentCommentId, Integer pageSize, Integer pageNumber, String order) {
        GetCommentsResponse response = new GetCommentsResponse();
        Sort sort = "DESC".equalsIgnoreCase(order) ? Sort.by("CREATED_DATE").descending() : Sort.by("CREATED_DATE").ascending();
        Page<CommentProjection> commentProjections = commentRepository.findCommentsByCommentParentId(parentCommentId , PageRequest.of(pageNumber, pageSize, sort));
        response.setResponse(commentProjections);
        return response;
    }

    @Override
    public GetCommentResponse updateComment(Long commentId, UpdateCommentRequest request) {
        commentRepository.updateCommentById(commentId, request.getUpdatedContent());
        GetCommentResponse response = new GetCommentResponse();
        CommentProjection commentProjection = commentRepository.findCommentByCommentId(commentId);
        response.setCommentProjection(commentProjection);
        return response;
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).get();
    }

    public CommentProjection getCommentProjectionById(Long commentId) {
        return commentRepository.findCommentByCommentId(commentId);
    }
}
