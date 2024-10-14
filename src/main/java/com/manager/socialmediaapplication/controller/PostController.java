package com.manager.socialmediaapplication.controller;

import com.manager.socialmediaapplication.dto.request.PostCreationRequest;
import com.manager.socialmediaapplication.dto.request.UpdatePostRequest;
import com.manager.socialmediaapplication.dto.response.GetPostResponse;
import com.manager.socialmediaapplication.dto.response.GetPostsResponse;

import com.manager.socialmediaapplication.service.intrface.PostServiceInterface;
import com.manager.socialmediaapplication.service.validation.CommentValidationService;
import com.manager.socialmediaapplication.service.validation.EndUserValidationService;
import com.manager.socialmediaapplication.service.validation.PostValidationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostServiceInterface postServiceInterface;
    EndUserValidationService endUserValidationService;
    PostValidationService postValidationService;
    private final CommentValidationService commentValidationService;

    @Autowired
    public PostController(PostServiceInterface postServiceInterface, EndUserValidationService endUserValidationService, PostValidationService postValidationService, CommentValidationService commentValidationService) {
        this.postServiceInterface = postServiceInterface;
        this.endUserValidationService = endUserValidationService;
        this.postValidationService = postValidationService;
        this.commentValidationService = commentValidationService;
    }

    @PostMapping
    public ResponseEntity<GetPostResponse> createPost(@Valid @RequestBody PostCreationRequest postCreationRequest) {
        endUserValidationService.doesUserExist(postCreationRequest.getUserId());
        GetPostResponse response = postServiceInterface.createPost(postCreationRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<GetPostResponse> updatePost(@PathVariable long postId, @Valid @RequestBody UpdatePostRequest request) {
        postValidationService.doesPostExist(postId);
        return ResponseEntity.ok(postServiceInterface.updatePost(postId, request));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<GetPostResponse> getPost(@PathVariable long postId) {
        postValidationService.doesPostExist(postId);
        return ResponseEntity.ok(postServiceInterface.getPost(postId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<GetPostsResponse> getPostForUser(@PathVariable long userId,
                                                           @RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize,
                                                           @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                           @RequestParam(value = "sorting", defaultValue = "DESC", required = false) String order) {
        endUserValidationService.doesUserExist(userId);
        return ResponseEntity.ok(postServiceInterface.getPostForUserId(userId, pageSize, pageNo, order));
    }

    @GetMapping
    public ResponseEntity<GetPostsResponse> getAllPosts(@RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize,
                                                        @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                        @RequestParam(value = "sorting", defaultValue = "DESC", required = false) String order) {
        return ResponseEntity.ok(postServiceInterface.getPosts(pageSize, pageNo, order));
    }
}
