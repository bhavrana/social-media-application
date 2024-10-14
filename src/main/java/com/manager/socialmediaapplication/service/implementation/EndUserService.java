package com.manager.socialmediaapplication.service.implementation;

import com.manager.socialmediaapplication.dto.request.EndUserCreationRequest;
import com.manager.socialmediaapplication.dto.response.GetEndUserResponse;
import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;
import com.manager.socialmediaapplication.exception.UserCreationException;
import com.manager.socialmediaapplication.model.*;
import com.manager.socialmediaapplication.model.projection.EndUserProjection;
import com.manager.socialmediaapplication.repository.EndUserRepository;
import com.manager.socialmediaapplication.service.intrface.EndUserServiceInterface;
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
public class EndUserService implements EndUserServiceInterface {

    EndUserRepository endUserRepository;
    PostUserInteractionService postUserInteractionService;
    CommentUserInteractionService commentUserInteractionService;

    @Autowired
    public EndUserService(EndUserRepository endUserRepository, PostUserInteractionService postUserInteractionService, CommentUserInteractionService commentUserInteractionService) {
        this.endUserRepository = endUserRepository;
        this.postUserInteractionService = postUserInteractionService;
        this.commentUserInteractionService = commentUserInteractionService;
    }

    @Override
    public GetEndUserResponse createEndUser(EndUserCreationRequest endUserCreationRequest) {
        EndUser endUser = new EndUser();
        endUser.setEmail(endUserCreationRequest.getEmail());
        endUser.setName(endUserCreationRequest.getName());
        try {
            EndUser newUser = endUserRepository.save(endUser);
            GetEndUserResponse response = new GetEndUserResponse();
            EndUserProjection endUserProjection = endUserRepository.findEndUserById(newUser.getId());
            response.setEndUserProjection(endUserProjection);
            return response;
        } catch (Exception e) {
            log.error("Error creating end user", e);
            throw new UserCreationException(e.getMessage());
        }
    }

    @Override
    public GetEndUsersResponse getEndUsers(Integer pageNo, Integer pageSize, String orderBy) {
        GetEndUsersResponse response = new GetEndUsersResponse();
        Sort sort = "DESC".equalsIgnoreCase(orderBy) ? Sort.by("CREATED_DATE").descending() : Sort.by("CREATED_DATE").ascending();
        Page<EndUserProjection> endUsers = endUserRepository.findAllProjectedBy(PageRequest.of(pageNo, pageSize, sort));
        response.setEndUserList(endUsers);
        return response;
    }

    @Override
    public GetEndUserResponse getEndUserResponseById(long userId) {
        GetEndUserResponse response = new GetEndUserResponse();
        EndUserProjection endUserProjection = endUserRepository.findEndUserById(userId);
        response.setEndUserProjection(endUserProjection);
        return response;
    }

    public EndUser getEndUserById(Long userId) {
        return  endUserRepository.findById(userId).get();
    }
}
