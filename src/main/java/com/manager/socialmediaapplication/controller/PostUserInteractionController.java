package com.manager.socialmediaapplication.controller;

import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;
import com.manager.socialmediaapplication.dto.response.GetPostResponse;
import com.manager.socialmediaapplication.service.intrface.PostServiceInterface;
import com.manager.socialmediaapplication.service.intrface.PostUserInteractionerviceInterface;
import com.manager.socialmediaapplication.service.validation.EndUserValidationService;
import com.manager.socialmediaapplication.service.validation.PostValidationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post/interact")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostUserInteractionController {
    PostUserInteractionerviceInterface postUserInteractionerviceInterface;
    PostServiceInterface postServiceInterface;
    PostValidationService postValidationService;
    EndUserValidationService endUserValidationService;

    @Autowired
    public PostUserInteractionController(PostUserInteractionerviceInterface postUserInteractionerviceInterface, PostServiceInterface postServiceInterface, PostValidationService postValidationService, EndUserValidationService endUserValidationService) {
        this.postUserInteractionerviceInterface = postUserInteractionerviceInterface;
        this.postServiceInterface = postServiceInterface;
        this.postValidationService = postValidationService;
        this.endUserValidationService = endUserValidationService;
    }

    @GetMapping("/like/{postId}")
    public ResponseEntity<GetEndUsersResponse> getUserWhoLikedPostById(@PathVariable long postId,
                                                                       @RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize,
                                                                       @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo) {
        postValidationService.doesPostExist(postId);
        return ResponseEntity.ok(postUserInteractionerviceInterface.getUserWhoLikedPostById(postId, pageNo, pageSize));
    }

    @GetMapping("/dislike/{postId}")
    public ResponseEntity<GetEndUsersResponse> getUserWhoDislikedPostById(@PathVariable long postId,
                                                                          @RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize,
                                                                          @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo) {
        postValidationService.doesPostExist(postId);
        return ResponseEntity.ok(postUserInteractionerviceInterface.getUserWhoDislikedPostById(postId, pageNo, pageSize));
    }

    @PutMapping("/like")
    public ResponseEntity<GetPostResponse> likePost(@RequestParam(name = "post_id") long postId, @RequestParam(name = "user_id") long userId) {
        postValidationService.doesPostExist(postId);
        endUserValidationService.doesUserExist(userId);
        postUserInteractionerviceInterface.likePost(postId, userId);
        return ResponseEntity.ok(postServiceInterface.getPost(postId));
    }

    @PutMapping("/dislike")
    public ResponseEntity<GetPostResponse> dislikePost(@RequestParam(name = "post_id") long postId, @RequestParam(name = "user_id") long userId) {
        postValidationService.doesPostExist(postId);
        endUserValidationService.doesUserExist(userId);
        postUserInteractionerviceInterface.dislikePost(postId, userId);
        return ResponseEntity.ok(postServiceInterface.getPost(postId));
    }

    @PutMapping("/remove-like")
    public ResponseEntity<GetPostResponse> removeLikePost(@RequestParam(name = "post_id") long postId, @RequestParam(name = "user_id") long userId) {
        postValidationService.doesPostExist(postId);
        endUserValidationService.doesUserExist(userId);
        postUserInteractionerviceInterface.removeLikePost(postId, userId);
        return ResponseEntity.ok(postServiceInterface.getPost(postId));
    }

    @PutMapping("/remove-dislike")
    public ResponseEntity<GetPostResponse> removeDislikePost(@RequestParam(name = "post_id") long postId, @RequestParam(name = "user_id") long userId) {
        postValidationService.doesPostExist(postId);
        endUserValidationService.doesUserExist(userId);
        postUserInteractionerviceInterface.removeDislikePost(postId, userId);
        return ResponseEntity.ok(postServiceInterface.getPost(postId));
    }
}
