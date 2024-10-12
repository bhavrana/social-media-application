package com.manager.socialmediaapplication.service.implementation;

import com.manager.socialmediaapplication.dto.request.PostCreationRequest;
import com.manager.socialmediaapplication.dto.request.UpdatePostRequest;
import com.manager.socialmediaapplication.dto.response.GetPostResponse;
import com.manager.socialmediaapplication.dto.response.GetPostsResponse;
import com.manager.socialmediaapplication.exception.PostNotFound;
import com.manager.socialmediaapplication.exception.UserNotFoundException;
import com.manager.socialmediaapplication.model.EndUser;
import com.manager.socialmediaapplication.model.Post;
import com.manager.socialmediaapplication.model.projection.PostProjection;
import com.manager.socialmediaapplication.repository.EndUserRepository;
import com.manager.socialmediaapplication.repository.PostRepository;
import com.manager.socialmediaapplication.service.validation.EndUserValidationService;
import com.manager.socialmediaapplication.service.validation.PostValidationService;
import com.manager.socialmediaapplication.service.view.PostServiceView;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService implements PostServiceView {
    PostRepository postRepository;
    EndUserValidationService endUserValidationService;
    EndUserRepository endUserRepository;
    PostValidationService postValidationService;

    @Autowired
    public PostService(PostRepository postRepository, EndUserValidationService endUserValidationService, EndUserRepository endUserRepository, PostValidationService postValidationService) {
        this.postRepository = postRepository;
        this.endUserValidationService = endUserValidationService;
        this.endUserRepository = endUserRepository;
        this.postValidationService = postValidationService;
    }

    @Override
    public void createPost(PostCreationRequest request) {
        if (!endUserValidationService.doesUserExist(request.getUserId())) {
            throw new UserNotFoundException("User not found for: " + request.getUserId());
        }
        EndUser endUser = endUserRepository.findById(request.getUserId()).get();
        Post post = new Post();
        post.setContent(request.getContent());
        post.setEndUser(endUser);
        postRepository.save(post);
    }

    @Override
    public GetPostsResponse getPosts() {
        GetPostsResponse response = new GetPostsResponse();
        List<PostProjection> postResponses = postRepository.getPosts();
        response.setResponse(postResponses);
        return response;
    }

    @Override
    public GetPostResponse getPost(Long postId) {
        if (!postValidationService.doesPostExist(postId)) {
            throw new PostNotFound("Post not found for: " + postId);
        }
        GetPostResponse response = new GetPostResponse();
        PostProjection postProjection = postRepository.findPostByPostId(postId);
        response.setPostProjection(postProjection);
        return response;
    }

    @Override
    public GetPostResponse updatePost(long postId, UpdatePostRequest request) {
        if (!postValidationService.doesPostExist(postId)) {
            throw new PostNotFound("Post not found for: " + postId);
        }
        postRepository.updatePostById(postId, request.getUpdatedContent());
        return getPost(postId);
    }

    @Override
    public void deletePost(Long id) {
        if (!postValidationService.doesPostExist(id)) {
            throw new PostNotFound("Post not found for: " + id);
        }
        postRepository.deleteById(id);
    }
}
