package com.manager.socialmediaapplication.controller;

import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;
import com.manager.socialmediaapplication.dto.response.GetPostResponse;
import com.manager.socialmediaapplication.service.view.PostUserInteractServiceView;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interact")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostUserInteractionController {
    PostUserInteractServiceView postUserInteractServiceView;

    @Autowired
    public PostUserInteractionController(PostUserInteractServiceView postUserInteractServiceView) {
        this.postUserInteractServiceView = postUserInteractServiceView;
    }

    @GetMapping("/like/{postId}")
    public ResponseEntity<GetEndUsersResponse> getUserWhoLikedPostById(@PathVariable long postId) {
        return ResponseEntity.ok(postUserInteractServiceView.getUserWhoLikedPostById(postId));
    }

    @GetMapping("/dislike/{postId}")
    public ResponseEntity<GetEndUsersResponse> getUserWhoDislikedPostById(@PathVariable long postId) {
        return ResponseEntity.ok(postUserInteractServiceView.getUserWhoDislikedPostById(postId));
    }

    @PutMapping("/like")
    public ResponseEntity<GetPostResponse> likePost(@RequestParam(name = "post_id") long postId, @RequestParam(name = "user_id") long userId) {
        return ResponseEntity.ok(postUserInteractServiceView.likePost(postId, userId));
    }

    @PutMapping("/dislike")
    public ResponseEntity<GetPostResponse> dislikePost(@RequestParam(name = "post_id") long postId, @RequestParam(name = "user_id") long userId) {
        return ResponseEntity.ok(postUserInteractServiceView.dislikePost(postId, userId));
    }
}
