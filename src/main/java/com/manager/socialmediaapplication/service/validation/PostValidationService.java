package com.manager.socialmediaapplication.service.validation;

import com.manager.socialmediaapplication.exception.PostNotFoundException;
import com.manager.socialmediaapplication.repository.PostRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostValidationService {
    PostRepository postRepository;

    public PostValidationService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void doesPostExist(long postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post not found for Id " + postId);
        }
    }
}
