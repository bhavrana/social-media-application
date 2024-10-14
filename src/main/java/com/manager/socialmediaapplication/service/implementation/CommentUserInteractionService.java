package com.manager.socialmediaapplication.service.implementation;

import com.manager.socialmediaapplication.dto.response.GetCommentResponse;
import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;
import com.manager.socialmediaapplication.model.*;
import com.manager.socialmediaapplication.model.projection.CommentProjection;
import com.manager.socialmediaapplication.model.projection.EndUserProjection;
import com.manager.socialmediaapplication.repository.CommentReactionRepository;
import com.manager.socialmediaapplication.repository.CommentUserInteractionRepository;
import com.manager.socialmediaapplication.repository.EndUserRepository;
import com.manager.socialmediaapplication.service.intrface.CommentUserInteractionServiceInterface;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentUserInteractionService implements CommentUserInteractionServiceInterface {
    CommentUserInteractionRepository commentUserInteractionRepository;
    CommentReactionRepository commentReactionRepository;
    CommentService commentService;
    EndUserRepository endUserRepository;

    @Autowired
    public CommentUserInteractionService(CommentUserInteractionRepository commentUserInteractionRepository, CommentReactionRepository commentReactionRepository, CommentService commentService, EndUserRepository endUserRepository) {
        this.commentUserInteractionRepository = commentUserInteractionRepository;
        this.commentReactionRepository = commentReactionRepository;
        this.commentService = commentService;
        this.endUserRepository = endUserRepository;
    }

    @Override
    public GetCommentResponse likeComment(Long commentId, Long userId) {
        // check if active entry exists under postUserInteraction
        Optional<CommentUserInteraction> optionalCommentUserInteraction = commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true,  commentId);
        Optional<CommentReaction> optionalCommentReaction = commentReactionRepository.findByComment_Id(commentId);
        if (optionalCommentUserInteraction.isPresent() && optionalCommentReaction.isPresent()) {
            CommentUserInteraction commentUserInteraction = optionalCommentUserInteraction.get();

            CommentReaction commentReaction = optionalCommentReaction.get();

            if (!commentUserInteraction.getIsLiked()) {
                commentUserInteraction.setIsLiked(true);
                Long likeCount = commentReaction.getLikeCount();
                commentReaction.setLikeCount(likeCount + 1);
                Long dislikeCount = commentReaction.getDislikeCount();
                commentReaction.setDislikeCount(dislikeCount - 1);
                commentUserInteractionRepository.save(commentUserInteraction);
                commentReactionRepository.save(commentReaction);
            } else {
                log.info("The Comment is already liked!");
            }

        } else {
            Comment comment = commentService.getCommentById(commentId);
            EndUser endUser = endUserRepository.findById(userId).get();
            CommentUserInteraction commentUserInteraction = new CommentUserInteraction();
            commentUserInteraction.setComment(comment);
            commentUserInteraction.setIsLiked(true);
            commentUserInteraction.setEndUser(endUser);
            commentUserInteraction.setIsActive(true);
            commentUserInteractionRepository.save(commentUserInteraction);
            // update post reaction counter
            if (optionalCommentReaction.isPresent()) {
                CommentReaction commentReaction = optionalCommentReaction.get();
                Long likeCount = commentReaction.getLikeCount();
                commentReaction.setLikeCount(likeCount + 1);
                commentReactionRepository.save(commentReaction);
            } else {
                CommentReaction commentReaction = new CommentReaction();
                commentReaction.setComment(comment);
                commentReaction.setLikeCount(1L);
                commentReaction.setDislikeCount(0L);
                commentReactionRepository.save(commentReaction);
            }
        }
        CommentProjection commentProjection = commentService.getCommentProjectionById(commentId);
        GetCommentResponse getCommentResponse = new GetCommentResponse();
        getCommentResponse.setCommentProjection(commentProjection);
        return getCommentResponse;
    }

    @Override
    public GetCommentResponse dislikeComment(Long commentId, Long userId) {
        // check if active entry exists under postUserInteraction
        Optional<CommentUserInteraction> optionalCommentUserInteraction = commentUserInteractionRepository.findByEndUser_IdAndIsActiveAndComment_Id(userId, true,  commentId);
        Optional<CommentReaction> optionalCommentReaction = commentReactionRepository.findByComment_Id(commentId);
        if (optionalCommentUserInteraction.isPresent() && optionalCommentReaction.isPresent()) {
            CommentUserInteraction commentUserInteraction = optionalCommentUserInteraction.get();

            CommentReaction commentReaction = optionalCommentReaction.get();

            if (commentUserInteraction.getIsLiked()) {
                commentUserInteraction.setIsLiked(false);
                Long dislikeCount = commentReaction.getDislikeCount();
                commentReaction.setDislikeCount(dislikeCount + 1);
                Long likeCount = commentReaction.getLikeCount();
                commentReaction.setLikeCount(likeCount - 1);
                commentUserInteractionRepository.save(commentUserInteraction);
                commentReactionRepository.save(commentReaction);
            } else {
                log.info("The Comment is already liked!");
            }

        } else {
            Comment comment = commentService.getCommentById(commentId);
            EndUser endUser = endUserRepository.findById(userId).get();
            CommentUserInteraction commentUserInteraction = new CommentUserInteraction();
            commentUserInteraction.setComment(comment);
            commentUserInteraction.setIsLiked(false);
            commentUserInteraction.setEndUser(endUser);
            commentUserInteraction.setIsActive(true);
            commentUserInteractionRepository.save(commentUserInteraction);
            // update post reaction counter
            if (optionalCommentReaction.isPresent()) {
                CommentReaction commentReaction = optionalCommentReaction.get();
                Long dislikeCount = commentReaction.getDislikeCount();
                commentReaction.setDislikeCount(dislikeCount + 1);
                commentReactionRepository.save(commentReaction);
            } else {
                CommentReaction commentReaction = new CommentReaction();
                commentReaction.setComment(comment);
                commentReaction.setLikeCount(0L);
                commentReaction.setDislikeCount(1L);
                commentReactionRepository.save(commentReaction);
            }
        }
        CommentProjection commentProjection = commentService.getCommentProjectionById(commentId);
        GetCommentResponse getCommentResponse = new GetCommentResponse();
        getCommentResponse.setCommentProjection(commentProjection);
        return getCommentResponse;
    }

    @Override
    public GetEndUsersResponse getUserWhoLikedCommentById(Long commentId, Integer pageNo, Integer pageSize) {
        GetEndUsersResponse response = new GetEndUsersResponse();
        Page<EndUserProjection> endUserProjectionList = commentUserInteractionRepository.getUserLikeInteractionByCommentId(commentId, PageRequest.of(pageNo, pageSize));
        response.setEndUserList(endUserProjectionList);
        return response;
    }

    @Override
    public GetEndUsersResponse getUserWhoDislikedCommentById(Long commentId, Integer pageNo, Integer pageSize) {
        GetEndUsersResponse response = new GetEndUsersResponse();
        Page<EndUserProjection> endUserProjectionList = commentUserInteractionRepository.getUserDislikeInteractionByCommentId(commentId, PageRequest.of(pageNo, pageSize));
        response.setEndUserList(endUserProjectionList);
        return response;
    }

    @Override
    public GetCommentResponse removeLikeComment(long commentId, long userId) {
        // check if active entry exists under postUserInteraction
        Optional<CommentUserInteraction> optionalCommentUserInteraction = commentUserInteractionRepository
                .findByEndUser_IdAndIsActiveAndComment_Id(userId, true,  commentId);
        Optional<CommentReaction> optionalCommentReaction = commentReactionRepository.findByComment_Id(commentId);
        if (optionalCommentUserInteraction.isPresent() && optionalCommentReaction.isPresent()) {
            CommentUserInteraction commentUserInteraction = optionalCommentUserInteraction.get();

            CommentReaction commentReaction = optionalCommentReaction.get();
            if (commentUserInteraction.getIsLiked()) {
                commentUserInteraction.setIsActive(false);
                Long likeCount = commentReaction.getLikeCount();
                commentReaction.setLikeCount(likeCount - 1);
                commentUserInteractionRepository.save(commentUserInteraction);
                if (commentReaction.getLikeCount() == 0 && commentReaction.getDislikeCount() == 0) {
                    deleteCommentReactionById(commentReaction.getId());
                } else {
                    commentReactionRepository.save(commentReaction);
                }
            }
        }
        CommentProjection commentProjection = commentService.getCommentProjectionById(commentId);
        GetCommentResponse getCommentResponse = new GetCommentResponse();
        getCommentResponse.setCommentProjection(commentProjection);
        return getCommentResponse;
    }

    @Override
    public GetCommentResponse removeDislikeComment(long commentId, long userId) {
        Optional<CommentUserInteraction> optionalCommentUserInteraction = commentUserInteractionRepository
                .findByEndUser_IdAndIsActiveAndComment_Id(userId, true,  commentId);
        Optional<CommentReaction> optionalCommentReaction = commentReactionRepository.findByComment_Id(commentId);
        if (optionalCommentUserInteraction.isPresent() && optionalCommentReaction.isPresent()) {
            CommentUserInteraction commentUserInteraction = optionalCommentUserInteraction.get();
            CommentReaction commentReaction = optionalCommentReaction.get();
            if (!commentUserInteraction.getIsLiked()) {
                commentUserInteraction.setIsActive(false);

                Long dislikeCount = commentReaction.getDislikeCount();
                commentReaction.setDislikeCount(dislikeCount - 1);
                commentUserInteractionRepository.save(commentUserInteraction);
                if (commentReaction.getLikeCount() == 0 && commentReaction.getDislikeCount() == 0) {
                    deleteCommentReactionById(commentReaction.getId());
                } else {
                    commentReactionRepository.save(commentReaction);
                }
            }
        }
        CommentProjection commentProjection = commentService.getCommentProjectionById(commentId);
        GetCommentResponse getCommentResponse = new GetCommentResponse();
        getCommentResponse.setCommentProjection(commentProjection);
        return getCommentResponse;
    }

    void deleteCommentReactionById(Long id) {
        commentReactionRepository.deleteById(id);
    }
}
