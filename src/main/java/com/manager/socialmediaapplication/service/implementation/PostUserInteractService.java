package com.manager.socialmediaapplication.service.implementation;

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
import com.manager.socialmediaapplication.service.intrface.PostUserInteractServiceInterface;
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
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostUserInteractService implements PostUserInteractServiceInterface {
    PostUserInteractionRepository postUserInteractionRepository;
    PostReactionRepository postReactionRepository;
    PostRepository postRepository;
    EndUserRepository endUserRepository;

    @Autowired
    public PostUserInteractService(PostUserInteractionRepository postUserInteractionRepository, PostReactionRepository postReactionRepository, PostRepository postRepository, EndUserRepository endUserRepository) {
        this.postUserInteractionRepository = postUserInteractionRepository;
        this.postReactionRepository = postReactionRepository;
        this.postRepository = postRepository;
        this.endUserRepository = endUserRepository;
    }

    @Override
    public void likePost(Long postId, Long userId) {
        // check if active entry exists under postUserInteraction
        Optional<PostUserInteraction> optionalPostUserInteraction = postUserInteractionRepository
                .findByEndUser_IdAndIsActiveAndPost_Id(userId, true,  postId);
        Optional<PostReaction> optionalPostReaction = postReactionRepository.findByPost_Id(postId);
        if (optionalPostUserInteraction.isPresent()) {
            PostUserInteraction postUserInteraction = optionalPostUserInteraction.get();

            PostReaction postReaction = optionalPostReaction.get();

            if (!postUserInteraction.getIsLiked()) {
                postUserInteraction.setIsLiked(true);
                Long likeCount = postReaction.getLikeCount();
                postReaction.setLikeCount(likeCount + 1);
                Long dislikeCount = postReaction.getDislikeCount();
                postReaction.setDislikeCount(dislikeCount - 1);
                postUserInteractionRepository.save(postUserInteraction);
                postReactionRepository.save(postReaction);
            } else {
                log.info("The Post is already liked!");
            }

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
    }

    @Override
    public void dislikePost(Long postId, Long userId) {
        Optional<PostUserInteraction> optionalPostUserInteraction = postUserInteractionRepository
                .findByEndUser_IdAndIsActiveAndPost_Id(userId, true,  postId);
        Optional<PostReaction> optionalPostReaction = postReactionRepository.findByPost_Id(postId);
        if (optionalPostUserInteraction.isPresent()) {
            PostUserInteraction postUserInteraction = optionalPostUserInteraction.get();
            PostReaction postReaction = optionalPostReaction.get();
            if (postUserInteraction.getIsLiked()) {
                postUserInteraction.setIsLiked(false);
                Long dislikeCount = postReaction.getDislikeCount();
                postReaction.setDislikeCount(dislikeCount + 1);
                Long likeCount = postReaction.getLikeCount();
                postReaction.setLikeCount(likeCount - 1);
                postUserInteractionRepository.save(postUserInteraction);
                postReactionRepository.save(postReaction);
            } else {
                log.info("The Post is already disliked!");
            }

        } else {
            // Add dislike for post for the user
            Post post =   postRepository.findById(postId).get();
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
    }

    @Override
    public GetEndUsersResponse getUserWhoLikedPostById(Long postId, Integer pageNo, Integer pageSize) {
        GetEndUsersResponse response = new GetEndUsersResponse();
        Page<EndUserProjection> endUserProjectionList = postUserInteractionRepository.getUserLikeInteractionByPostId(postId, PageRequest.of(pageNo, pageSize));
        response.setEndUserList(endUserProjectionList);
        return response;
    }

    @Override
    public GetEndUsersResponse getUserWhoDislikedPostById(Long postId, Integer pageNo, Integer pageSize) {
        GetEndUsersResponse response = new GetEndUsersResponse();
        Page<EndUserProjection> endUserProjectionList = postUserInteractionRepository.getUserDislikeInteractionByPostId(postId, PageRequest.of(pageNo, pageSize));
        response.setEndUserList(endUserProjectionList);
        return response;
    }

    @Override
    public void removeLikePost(long postId, long userId) {
        // check if active entry exists under postUserInteraction
        Optional<PostUserInteraction> optionalPostUserInteraction = postUserInteractionRepository
                .findByEndUser_IdAndIsActiveAndPost_Id(userId, true,  postId);
        Optional<PostReaction> optionalPostReaction = postReactionRepository.findByPost_Id(postId);
        if (optionalPostUserInteraction.isPresent()) {
            PostUserInteraction postUserInteraction = optionalPostUserInteraction.get();

            PostReaction postReaction = optionalPostReaction.get();
            if (postUserInteraction.getIsLiked()) {
                postUserInteraction.setIsActive(false);
                Long likeCount = postReaction.getLikeCount();
                postReaction.setLikeCount(likeCount - 1);
                postUserInteractionRepository.save(postUserInteraction);
                postReactionRepository.save(postReaction);
            }
        }
    }

    @Override
    public void removeDislikePost(long postId, long userId) {
        // check if active entry exists under postUserInteraction
        Optional<PostUserInteraction> optionalPostUserInteraction = postUserInteractionRepository
                .findByEndUser_IdAndIsActiveAndPost_Id(userId, true,  postId);
        Optional<PostReaction> optionalPostReaction = postReactionRepository.findByPost_Id(postId);
        if (optionalPostUserInteraction.isPresent()) {
            PostUserInteraction postUserInteraction = optionalPostUserInteraction.get();
            PostReaction postReaction = optionalPostReaction.get();
            if (!postUserInteraction.getIsLiked()) {
                postUserInteraction.setIsActive(false);

                Long dislikeCount = postReaction.getDislikeCount();
                postReaction.setDislikeCount(dislikeCount - 1);
                postUserInteractionRepository.save(postUserInteraction);
                postReactionRepository.save(postReaction);
            }
        }
    }

    List<PostUserInteraction> getPostUserInteractionByUserId(long userId) {
        return postUserInteractionRepository.findByEndUser_IdAndIsActive(userId, true);
    }

    Optional<PostReaction> getPostReactionByPostId(Long id) {
        return postReactionRepository.findByPost_Id(id);
    }

    void deletePostReactionById(Long id) {
        postReactionRepository.deleteById(id);
    }

    void savePostReaction(PostReaction postReaction) {
        postReactionRepository.save(postReaction);
    }
}
