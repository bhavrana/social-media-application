package com.manager.socialmediaapplication.service.implementation;

import com.manager.socialmediaapplication.dto.request.PostCreationRequest;
import com.manager.socialmediaapplication.dto.request.UpdatePostRequest;
import com.manager.socialmediaapplication.dto.response.GetPostResponse;
import com.manager.socialmediaapplication.dto.response.GetPostsResponse;
import com.manager.socialmediaapplication.model.EndUser;
import com.manager.socialmediaapplication.model.Post;
import com.manager.socialmediaapplication.model.projection.PostProjection;
import com.manager.socialmediaapplication.repository.PostRepository;
import com.manager.socialmediaapplication.service.intrface.PostServiceInterface;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService implements PostServiceInterface {
    PostRepository postRepository;
    EndUserService endUserService;
    CommentService commentService;

    @Autowired
    public PostService(PostRepository postRepository, EndUserService endUserService, CommentService commentService) {
        this.postRepository = postRepository;
        this.endUserService = endUserService;
        this.commentService = commentService;
    }

    @Override
    public GetPostResponse createPost(PostCreationRequest request) {
        log.info("Creating a new post for user ID: {}", request.getUserId());
        EndUser endUser = endUserService.getEndUserById(request.getUserId());
        Post post = new Post();
        post.setContent(request.getContent());
        post.setEndUser(endUser);
        Post newPost = postRepository.save(post);
        log.info("Successfully created post with ID: {}", newPost.getId());
        GetPostResponse response  = new GetPostResponse(postRepository.findPostByPostId(newPost.getId()));
        return response;
    }

    @Override
    public GetPostsResponse getPosts(Integer pageSize, Integer pageNo, String order) {
        log.info("Fetching posts with page size: {}, page number: {}, order: {}", pageSize, pageNo, order);
        GetPostsResponse response = new GetPostsResponse();
        Sort sort = "DESC".equalsIgnoreCase(order) ? Sort.by("CREATED_DATE").descending() : Sort.by("CREATED_DATE").ascending();
        Page<PostProjection> postResponses = postRepository.getPosts(PageRequest.of(pageNo, pageSize, sort));
        log.info("Fetched {} posts", postResponses.getTotalElements());
        response.setResponse(postResponses);
        return response;
    }

    @Override
    public GetPostResponse getPost(Long postId) {
        log.info("Fetching post by ID: {}", postId);
        GetPostResponse response = new GetPostResponse();
        PostProjection postProjection = postRepository.findPostByPostId(postId);
        response.setPostProjection(postProjection);
        return response;
    }

    @Override
    public GetPostResponse updatePost(long postId, UpdatePostRequest request) {
        log.info("Updating post ID: {} with new content", postId);
        postRepository.updatePostById(postId, request.getUpdatedContent());
        log.info("Successfully updated post ID: {}", postId);
        return getPost(postId);
    }

    @Override
    public GetPostsResponse getPostForUserId(long userId, Integer pageSize, Integer pageNo, String order) {
        log.info("Fetching posts for user ID: {} with page size: {}, page number: {}, order: {}", userId, pageSize, pageNo, order);
        GetPostsResponse response = new GetPostsResponse();
        Sort sort = "DESC".equalsIgnoreCase(order) ? Sort.by("CREATED_DATE").descending() : Sort.by("CREATED_DATE").ascending();
        Page<PostProjection> postResponses = postRepository.getPostForUserId(userId, PageRequest.of(pageNo, pageSize, sort));
        log.info("Fetched {} posts for user ID: {}", postResponses.getTotalElements(), userId);
        response.setResponse(postResponses);
        return response;
    }
}
