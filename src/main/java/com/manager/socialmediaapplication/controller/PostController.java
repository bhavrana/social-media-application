package com.manager.socialmediaapplication.controller;

import com.manager.socialmediaapplication.dto.request.PostCreationRequest;
import com.manager.socialmediaapplication.dto.request.UpdatePostRequest;
import com.manager.socialmediaapplication.dto.response.GetPostResponse;
import com.manager.socialmediaapplication.dto.response.GetPostsResponse;

import com.manager.socialmediaapplication.service.view.PostServiceView;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostServiceView postServiceView;

    @Autowired
    public PostController(PostServiceView postServiceView) {
        this.postServiceView = postServiceView;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createPost(@Valid @RequestBody PostCreationRequest postCreationRequest) {
        postServiceView.createPost(postCreationRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<GetPostResponse> updatePost(@PathVariable long postId, @Valid @RequestBody UpdatePostRequest request) {
        return ResponseEntity.ok(postServiceView.updatePost(postId, request));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<GetPostResponse> getPost(@PathVariable long postId) {
        return ResponseEntity.ok(postServiceView.getPost(postId));
    }

    @GetMapping
    public ResponseEntity<GetPostsResponse> getAllPosts() {
        return ResponseEntity.ok(postServiceView.getPosts());
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable long postId) {
        postServiceView.deletePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
