package com.manager.socialmediaapplication.service.implementation;

import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;
import com.manager.socialmediaapplication.dto.response.GetPostResponse;
import com.manager.socialmediaapplication.exception.PostNotFound;
import com.manager.socialmediaapplication.exception.UserNotFoundException;
import com.manager.socialmediaapplication.model.EndUser;
import com.manager.socialmediaapplication.model.Post;
import com.manager.socialmediaapplication.model.PostReaction;
import com.manager.socialmediaapplication.model.PostUserInteraction;
import com.manager.socialmediaapplication.model.projection.EndUserProjection;
import com.manager.socialmediaapplication.repository.EndUserRepository;
import com.manager.socialmediaapplication.repository.PostReactionRepository;
import com.manager.socialmediaapplication.repository.PostRepository;
import com.manager.socialmediaapplication.repository.PostUserInteractionRepository;
import com.manager.socialmediaapplication.service.validation.EndUserValidationService;
import com.manager.socialmediaapplication.service.validation.PostValidationService;
import com.manager.socialmediaapplication.service.view.PostUserInteractServiceView;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostUserInteractService implements PostUserInteractServiceView {
    PostValidationService postValidationService;
    PostUserInteractionRepository postUserInteractionRepository;
    PostRepository postRepository;
    EndUserRepository endUserRepository;
    PostReactionRepository postReactionRepository;
    EndUserValidationService endUserValidationService;
    PostService postService;

    @Autowired
    public PostUserInteractService(PostValidationService postValidationService, PostUserInteractionRepository postUserInteractionRepository, PostRepository postRepository, EndUserRepository endUserRepository, PostReactionRepository postReactionRepository, EndUserValidationService endUserValidationService, PostService postService) {
        this.postValidationService = postValidationService;
        this.postUserInteractionRepository = postUserInteractionRepository;
        this.postRepository = postRepository;
        this.endUserRepository = endUserRepository;
        this.postReactionRepository = postReactionRepository;
        this.endUserValidationService = endUserValidationService;
        this.postService = postService;
    }

    @Override
    public GetPostResponse likePost(Long postId, Long userId) {
        if (!postValidationService.doesPostExist(postId)) {
            throw new PostNotFound("Post not found for Id " + postId);
        }
        if(!endUserValidationService.doesUserExist(userId)) {
            throw new UserNotFoundException("User not found for Id " + userId);
        }
        // check if active entry exists under postUserInteraction
        Optional<PostUserInteraction> optionalPostUserInteraction = postUserInteractionRepository
                .findByEndUser_IdAndIsActiveAndPost_Id(userId, true,  postId);
        Optional<PostReaction> optionalPostReaction = postReactionRepository.findByPost_Id(postId);
        if (optionalPostUserInteraction.isPresent()) {
            // undo like by marking it as is_active false and decrement post like count
            PostUserInteraction postUserInteraction = optionalPostUserInteraction.get();

            PostReaction postReaction = optionalPostReaction.get();
            if (postUserInteraction.getIsLiked()) {
                postUserInteraction.setIsActive(false);

                Long likeCount = postReaction.getLikeCount();
                postReaction.setLikeCount(likeCount - 1);
            } else {
                postUserInteraction.setIsLiked(true);
                //inc like count
                Long likeCount = postReaction.getLikeCount();
                postReaction.setLikeCount(likeCount + 1);
                //dec dislike count
                Long dislikeCount = postReaction.getDislikeCount();
                postReaction.setDislikeCount(dislikeCount - 1);
            }
            postUserInteractionRepository.save(postUserInteraction);
            // update post reaction counter
            postReactionRepository.save(postReaction);
        } else {
            // Add like for post for the user
            Post post = postRepository.findById(postId).get();
            EndUser endUser = endUserRepository.findById(userId).get();
            PostUserInteraction postUserInteraction = new PostUserInteraction();
            postUserInteraction.setPost(post);
            postUserInteraction.setIsLiked(true);
            postUserInteraction.setEndUser(endUser);
            postUserInteraction.setIsActive(true);
            postUserInteractionRepository.save(postUserInteraction);
            // update post reaction counter
            if (optionalPostReaction.isPresent()) {
                PostReaction postReaction = optionalPostReaction.get();
                Long likeCount = postReaction.getLikeCount();
                postReaction.setLikeCount(likeCount + 1);
                postReactionRepository.save(postReaction);
            } else {
                PostReaction postReaction = new PostReaction();
                postReaction.setPost(post);
                postReaction.setLikeCount(1L);
                postReaction.setDislikeCount(0L);
                postReactionRepository.save(postReaction);
            }
        }
        return postService.getPost(postId);
    }

    @Override
    public GetPostResponse dislikePost(Long postId, Long userId) {
        if (!postValidationService.doesPostExist(postId)) {
            throw new PostNotFound("Post not found for: Id " + postId);
        }
        if(!endUserValidationService.doesUserExist(userId)) {
            throw new UserNotFoundException("User not found for Id " + userId);
        }
        Optional<PostUserInteraction> optionalPostUserInteraction = postUserInteractionRepository
                .findByEndUser_IdAndIsActiveAndPost_Id(userId, true,  postId);
        Optional<PostReaction> optionalPostReaction = postReactionRepository.findByPost_Id(postId);
        if (optionalPostUserInteraction.isPresent()) {
            // undo like by marking it as is_active false and decrement post like count
            PostUserInteraction postUserInteraction = optionalPostUserInteraction.get();

            PostReaction postReaction = optionalPostReaction.get();
            if (!postUserInteraction.getIsLiked()) {
                postUserInteraction.setIsActive(false);

                Long dislikeCount = postReaction.getDislikeCount();
                postReaction.setLikeCount(dislikeCount - 1);
            } else {
                postUserInteraction.setIsLiked(false);
                //inc dislike count
                Long dislikeCount = postReaction.getDislikeCount();
                postReaction.setDislikeCount(dislikeCount + 1);
                //dec like count
                Long likeCount = postReaction.getLikeCount();
                postReaction.setLikeCount(likeCount - 1);
            }
            postUserInteractionRepository.save(postUserInteraction);
            // update post reaction counter
            postReactionRepository.save(postReaction);
        } else {
            // Add dislike for post for the user
            Post post = postRepository.findById(postId).get();
            EndUser endUser = endUserRepository.findById(userId).get();
            PostUserInteraction postUserInteraction = new PostUserInteraction();
            postUserInteraction.setPost(post);
            postUserInteraction.setIsLiked(false);
            postUserInteraction.setEndUser(endUser);
            postUserInteraction.setIsActive(true);
            postUserInteractionRepository.save(postUserInteraction);
            // update post reaction counter
            if (optionalPostReaction.isPresent()) {
                PostReaction postReaction = optionalPostReaction.get();
                Long dislikeCount = postReaction.getDislikeCount();
                postReaction.setDislikeCount(dislikeCount + 1);
                postReactionRepository.save(postReaction);
            } else {
                PostReaction postReaction = new PostReaction();
                postReaction.setPost(post);
                postReaction.setLikeCount(0L);
                postReaction.setDislikeCount(1L);
                postReactionRepository.save(postReaction);
            }
        }
        return postService.getPost(postId);
    }

    @Override
    public GetEndUsersResponse getUserWhoLikedPostById(Long postId) {
        if (!postValidationService.doesPostExist(postId)) {
            throw new PostNotFound("Post not found for Id " + postId);
        }
        GetEndUsersResponse response = new GetEndUsersResponse();
        List<EndUserProjection> endUserProjectionList = postUserInteractionRepository.getUserLikeInteractionByPostId(postId);
        response.setEndUserList(endUserProjectionList);
        return response;
    }

    @Override
    public GetEndUsersResponse getUserWhoDislikedPostById(Long postId) {
        if (!postValidationService.doesPostExist(postId)) {
            throw new PostNotFound("Post not found for Id " + postId);
        }
        GetEndUsersResponse response = new GetEndUsersResponse();
        List<EndUserProjection> endUserProjectionList = postUserInteractionRepository.getUserDislikeInteractionByPostId(postId);
        response.setEndUserList(endUserProjectionList);
        return response;
    }
}
