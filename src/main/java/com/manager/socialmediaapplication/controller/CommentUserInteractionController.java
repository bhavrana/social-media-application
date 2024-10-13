package com.manager.socialmediaapplication.controller;

import com.manager.socialmediaapplication.dto.response.GetCommentResponse;
import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;
import com.manager.socialmediaapplication.service.intrface.CommentUserInteractionServiceInterface;
import com.manager.socialmediaapplication.service.validation.CommentValidationService;
import com.manager.socialmediaapplication.service.validation.EndUserValidationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment/interact")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentUserInteractionController {
    CommentUserInteractionServiceInterface commentUserInteractionServiceInterface;
    CommentValidationService commentValidationService;
    EndUserValidationService endUserValidationService;

    @Autowired
    public CommentUserInteractionController(CommentUserInteractionServiceInterface commentUserInteractionServiceInterface, CommentValidationService commentValidationService, EndUserValidationService endUserValidationService) {
        this.commentUserInteractionServiceInterface = commentUserInteractionServiceInterface;
        this.commentValidationService = commentValidationService;
        this.endUserValidationService = endUserValidationService;
    }

    @GetMapping("/like/{commentId}")
    public ResponseEntity<GetEndUsersResponse> getUserWhoLikedCommentById(@PathVariable long commentId,
                                                                          @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                          @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo) {
        commentValidationService.doesCommentExist(commentId);
        return ResponseEntity.ok(commentUserInteractionServiceInterface.getUserWhoLikedCommentById(commentId, pageNo, pageSize));
    }

    @GetMapping("/dislike/{commentId}")
    public ResponseEntity<GetEndUsersResponse> getUserWhoDislikedCommentById(@PathVariable long commentId,
                                                                             @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                                             @RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo) {
        commentValidationService.doesCommentExist(commentId);
        return ResponseEntity.ok(commentUserInteractionServiceInterface.getUserWhoDislikedCommentById(commentId, pageNo, pageSize));
    }

    @PutMapping("/like")
    public ResponseEntity<GetCommentResponse> likeComment(@RequestParam(name = "comment_id") long commentId, @RequestParam(name = "user_id") long userId) {
        commentValidationService.doesCommentExist(commentId);
        endUserValidationService.doesUserExist(userId);
        return ResponseEntity.ok(commentUserInteractionServiceInterface.likeComment(commentId, userId));
    }

    @PutMapping("/dislike")
    public ResponseEntity<GetCommentResponse> dislikeComment(@RequestParam(name = "comment_id") long commentId, @RequestParam(name = "user_id") long userId) {
        commentValidationService.doesCommentExist(commentId);
        endUserValidationService.doesUserExist(userId);
        return ResponseEntity.ok(commentUserInteractionServiceInterface.dislikeComment(commentId, userId));
    }

    @PutMapping("/remove-like")
    public ResponseEntity<GetCommentResponse> removeLikeComment(@RequestParam(name = "comment_id") long commentId, @RequestParam(name = "user_id") long userId) {
        commentValidationService.doesCommentExist(commentId);
        endUserValidationService.doesUserExist(userId);
        return ResponseEntity.ok(commentUserInteractionServiceInterface.removeLikeComment(commentId, userId));
    }

    @PutMapping("/remove-dislike")
    public ResponseEntity<GetCommentResponse> removeDislikeComment(@RequestParam(name = "comment_id") long commentId, @RequestParam(name = "user_id") long userId) {
        commentValidationService.doesCommentExist(commentId);
        endUserValidationService.doesUserExist(userId);
        return ResponseEntity.ok(commentUserInteractionServiceInterface.removeDislikeComment(commentId, userId));
    }

}
