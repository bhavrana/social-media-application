package com.manager.socialmediaapplication.controller;

import com.manager.socialmediaapplication.dto.request.CommentCreationRequest;
import com.manager.socialmediaapplication.dto.request.UpdateCommentRequest;
import com.manager.socialmediaapplication.dto.response.GetCommentResponse;
import com.manager.socialmediaapplication.dto.response.GetCommentsResponse;
import com.manager.socialmediaapplication.service.intrface.CommentServiceInterface;
import com.manager.socialmediaapplication.service.validation.CommentValidationService;
import com.manager.socialmediaapplication.service.validation.EndUserValidationService;
import com.manager.socialmediaapplication.service.validation.PostValidationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentServiceInterface commentServiceInterface;
    EndUserValidationService endUserValidationService;
    PostValidationService postValidationService;
    CommentValidationService commentValidationService;

    @Autowired
    public CommentController(CommentServiceInterface commentServiceInterface, EndUserValidationService endUserValidationService, PostValidationService postValidationService, CommentValidationService commentValidationService) {
        this.commentServiceInterface = commentServiceInterface;
        this.endUserValidationService = endUserValidationService;
        this.postValidationService = postValidationService;
        this.commentValidationService = commentValidationService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createComment(@Valid @RequestBody CommentCreationRequest request) {
        endUserValidationService.doesUserExist(request.getUserId());
        postValidationService.doesPostExist(request.getPostId());
        commentValidationService.isPostValid(request.getPostId(), request.getParentId());
        commentServiceInterface.createComment(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<GetCommentResponse> updateComment(@PathVariable long commentId, @Valid @RequestBody UpdateCommentRequest request) {
        commentValidationService.doesCommentExist(commentId);
        return ResponseEntity.ok(commentServiceInterface.updateComment(commentId, request));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<GetCommentsResponse> getCommentsForPost(@PathVariable long postId,
                                                                  @RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize,
                                                                  @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                                  @RequestParam(value = "sorting", defaultValue = "DESC", required = false) String order) {

        postValidationService.doesPostExist(postId);
        return ResponseEntity.ok(commentServiceInterface.getCommentsForPost(postId, pageSize, pageNo, order));
    }

    @GetMapping("/parent/{commentId}")
    public ResponseEntity<GetCommentsResponse> getCommentsForParentComment(@PathVariable long commentId,
                                                                           @RequestParam(value = "pageSize", defaultValue = "3", required = false) Integer pageSize,
                                                                           @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                                           @RequestParam(value = "sorting", defaultValue = "DESC", required = false) String order) {
        commentValidationService.doesCommentExist(commentId);
        return ResponseEntity.ok(commentServiceInterface.getCommentsForParent(commentId, pageSize, pageNo, order));
    }
}
