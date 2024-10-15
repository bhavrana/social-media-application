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
import com.manager.socialmediaapplication.service.intrface.PostUserInteractionerviceInterface;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostUserInteractionService implements PostUserInteractionerviceInterface {
    PostUserInteractionRepository postUserInteractionRepository;
    PostReactionRepository postReactionRepository;
    PostRepository postRepository;
    EndUserRepository endUserRepository;

    @Autowired
    public PostUserInteractionService(PostUserInteractionRepository postUserInteractionRepository, PostReactionRepository postReactionRepository, PostRepository postRepository, EndUserRepository endUserRepository) {
        this.postUserInteractionRepository = postUserInteractionRepository;
        this.postReactionRepository = postReactionRepository;
        this.postRepository = postRepository;
        this.endUserRepository = endUserRepository;
    }

    @Override
    public void likePost(Long postId, Long userId) {
        log.info("Attempting to like post ID: {} by user ID: {}", postId, userId);
        // check if active entry exists under postUserInteraction
        Optional<PostUserInteraction> optionalPostUserInteraction = postUserInteractionRepository
                .findByEndUser_IdAndIsActiveAndPost_Id(userId, true,  postId);
        Optional<PostReaction> optionalPostReaction = postReactionRepository.findByPost_Id(postId);
        if (optionalPostUserInteraction.isPresent() && optionalPostReaction.isPresent()) {
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
                log.info("Liked the post ID: {} by user ID: {}", postId, userId);
            } else {
                log.info("The post ID: {} is already liked by user ID: {}", postId, userId);
            }

        } else {
            log.info("Creating new like interaction for post ID: {} by user ID: {}", postId, userId);
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
        log.info("Attempting to dislike post ID: {} by user ID: {}", postId, userId);
        Optional<PostUserInteraction> optionalPostUserInteraction = postUserInteractionRepository
                .findByEndUser_IdAndIsActiveAndPost_Id(userId, true,  postId);
        Optional<PostReaction> optionalPostReaction = postReactionRepository.findByPost_Id(postId);
        if (optionalPostUserInteraction.isPresent() && optionalPostReaction.isPresent()) {
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
                log.info("Disliked the previously liked post ID: {} by user ID: {}", postId, userId);
            } else {
                log.info("The post ID: {} is already disliked by user ID: {}", postId, userId);
            }

        } else {
            log.info("Creating new dislike interaction for post ID: {} by user ID: {}", postId, userId);
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
        log.info("Fetching users who liked post ID: {} with page number: {} and page size: {}", postId, pageNo, pageSize);
        GetEndUsersResponse response = new GetEndUsersResponse();
        Page<EndUserProjection> endUserProjectionList = postUserInteractionRepository.getUserLikeInteractionByPostId(postId, PageRequest.of(pageNo, pageSize));
        log.info("Fetched {} users who liked the post ID: {}", endUserProjectionList.getTotalElements(), postId);
        response.setEndUserList(endUserProjectionList);
        return response;
    }

    @Override
    public GetEndUsersResponse getUserWhoDislikedPostById(Long postId, Integer pageNo, Integer pageSize) {
        log.info("Fetching users who disliked post ID: {} with page number: {} and page size: {}", postId, pageNo, pageSize);
        GetEndUsersResponse response = new GetEndUsersResponse();
        Page<EndUserProjection> endUserProjectionList = postUserInteractionRepository.getUserDislikeInteractionByPostId(postId, PageRequest.of(pageNo, pageSize));
        log.info("Fetched {} users who disliked the post ID: {}", endUserProjectionList.getTotalElements(), postId);
        response.setEndUserList(endUserProjectionList);
        return response;
    }

    @Override
    public void removeLikePost(long postId, long userId) {
        log.info("Attempting to remove like from post ID: {} by user ID: {}", postId, userId);
        // check if active entry exists under postUserInteraction
        Optional<PostUserInteraction> optionalPostUserInteraction = postUserInteractionRepository
                .findByEndUser_IdAndIsActiveAndPost_Id(userId, true,  postId);
        Optional<PostReaction> optionalPostReaction = postReactionRepository.findByPost_Id(postId);
        if (optionalPostUserInteraction.isPresent() && optionalPostReaction.isPresent()) {
            PostUserInteraction postUserInteraction = optionalPostUserInteraction.get();

            PostReaction postReaction = optionalPostReaction.get();
            if (postUserInteraction.getIsLiked()) {
                postUserInteraction.setIsActive(false);
                Long likeCount = postReaction.getLikeCount();
                postReaction.setLikeCount(likeCount - 1);
                log.info("Removed like from post ID: {} by user ID: {}", postId, userId);
                postUserInteractionRepository.save(postUserInteraction);
                if (postReaction.getLikeCount() == 0 && postReaction.getDislikeCount() == 0) {
                    deletePostReactionById(postReaction.getId());
                    log.info("Deleted reaction record for post ID: {} as it has no likes or dislikes", postId);
                } else {
                    postReactionRepository.save(postReaction);
                }
            }
        }
    }

    @Override
    public void removeDislikePost(long postId, long userId) {
        log.info("Attempting to remove dislike from post ID: {} by user ID: {}", postId, userId);
        // check if active entry exists under postUserInteraction
        Optional<PostUserInteraction> optionalPostUserInteraction = postUserInteractionRepository
                .findByEndUser_IdAndIsActiveAndPost_Id(userId, true,  postId);
        Optional<PostReaction> optionalPostReaction = postReactionRepository.findByPost_Id(postId);
        if (optionalPostUserInteraction.isPresent() && optionalPostReaction.isPresent()) {
            PostUserInteraction postUserInteraction = optionalPostUserInteraction.get();
            PostReaction postReaction = optionalPostReaction.get();
            if (!postUserInteraction.getIsLiked()) {
                postUserInteraction.setIsActive(false);
                Long dislikeCount = postReaction.getDislikeCount();
                postReaction.setDislikeCount(dislikeCount - 1);
                log.info("Removed dislike from post ID: {} by user ID: {}", postId, userId);
                postUserInteractionRepository.save(postUserInteraction);
                if (postReaction.getLikeCount() == 0 && postReaction.getDislikeCount() == 0) {
                    deletePostReactionById(postReaction.getId());
                    log.info("Deleted reaction record for post ID: {} as it has no likes or dislikes", postId);
                } else {
                    postReactionRepository.save(postReaction);
                }
            }
        }
    }

    void deletePostReactionById(Long id) {
        try {
            log.info("Deleting reaction record with ID: {}", id);
            postReactionRepository.deleteById(id);
            log.info("Successfully deleted reaction record with ID: {}", id);
        } catch (Exception e) {
            log.error("Failed to delete reaction record with ID: {}", id, e);
        }
    }
}
