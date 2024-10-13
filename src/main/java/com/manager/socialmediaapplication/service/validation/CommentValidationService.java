package com.manager.socialmediaapplication.service.validation;

import com.manager.socialmediaapplication.exception.CommentNotFoundException;
import com.manager.socialmediaapplication.exception.InvalidCommentPostException;
import com.manager.socialmediaapplication.repository.CommentRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentValidationService {
    CommentRepository commentRepository;

    @Autowired
    public CommentValidationService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void doesCommentExist(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException("Comment with id " + id + " does not exist");
        }
    }

    public void isPostValid(@NotNull(message = "Null value is not supported for Post ID") Long postId, Long parentId) {
        if (parentId == null) {
            return;
        }
        if (!commentRepository.existsById(parentId)) {
            throw new CommentNotFoundException("Comment with id " + parentId + " does not exist");
        }
        long parentPostId =commentRepository.findById(parentId).get().getPost().getId();
        if (parentPostId != postId) {
            throw new InvalidCommentPostException("Post ID of the parent: " + parentPostId + " is different from given post ID: " + postId);
        }
    }
}
