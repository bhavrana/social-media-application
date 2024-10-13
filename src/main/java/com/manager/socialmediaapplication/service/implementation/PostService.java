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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void createPost(PostCreationRequest request) {
        EndUser endUser = endUserService.getEndUserById(request.getUserId());
        Post post = new Post();
        post.setContent(request.getContent());
        post.setEndUser(endUser);
        postRepository.save(post);
    }

    @Override
    public GetPostsResponse getPosts(Integer pageSize, Integer pageNo, String order) {
        GetPostsResponse response = new GetPostsResponse();
        Sort sort = "DESC".equals(order) ? Sort.by("CREATED_DATE").descending() : Sort.by("CREATED_DATE").ascending();
        Page<PostProjection> postResponses = postRepository.getPosts(PageRequest.of(pageNo, pageSize, sort));
        response.setResponse(postResponses);
        return response;
    }

    @Override
    public GetPostResponse getPost(Long postId) {
        GetPostResponse response = new GetPostResponse();
        PostProjection postProjection = postRepository.findPostByPostId(postId);
        response.setPostProjection(postProjection);
        return response;
    }

    @Override
    public GetPostResponse updatePost(long postId, UpdatePostRequest request) {
        postRepository.updatePostById(postId, request.getUpdatedContent());
        return getPost(postId);
    }

    @Override
    public GetPostsResponse getPostForUserId(long userId, Integer pageSize, Integer pageNo, String order) {
        GetPostsResponse response = new GetPostsResponse();
        Sort sort = "DESC".equals(order) ? Sort.by("CREATED_DATE").descending() : Sort.by("CREATED_DATE").ascending();
        Page<PostProjection> postResponses = postRepository.getPostForUserId(userId, PageRequest.of(pageNo, pageSize, sort));
        response.setResponse(postResponses);
        return response;
    }
}
