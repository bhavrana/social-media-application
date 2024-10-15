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
        log.info("Creating new end user with email: {}", endUserCreationRequest.getEmail());
        EndUser endUser = new EndUser();
        endUser.setEmail(endUserCreationRequest.getEmail());
        endUser.setName(endUserCreationRequest.getName());
        try {
            EndUser newUser = endUserRepository.save(endUser);
            log.info("Successfully created end user with ID: {}", newUser.getId());
            GetEndUserResponse response = new GetEndUserResponse();
            EndUserProjection endUserProjection = endUserRepository.findEndUserById(newUser.getId());
            response.setEndUserProjection(endUserProjection);
            return response;
        } catch (Exception e) {
            log.error("Error creating end user with email: {}", endUserCreationRequest.getEmail(), e);
            throw new UserCreationException(e.getMessage());
        }
    }

    @Override
    public GetEndUsersResponse getEndUsers(Integer pageNo, Integer pageSize, String orderBy) {
        log.info("Fetching end users with page number: {}, page size: {}, order by: {}", pageNo, pageSize, orderBy);
        GetEndUsersResponse response = new GetEndUsersResponse();
        Sort sort = "DESC".equalsIgnoreCase(orderBy) ? Sort.by("id").descending() : Sort.by("id").ascending();
        Page<EndUserProjection> endUsers = endUserRepository.findAllProjectedBy(PageRequest.of(pageNo, pageSize, sort));
        log.info("Fetched {} end users", endUsers.getTotalElements());
        response.setEndUserList(endUsers);
        return response;
    }

    @Override
    public GetEndUserResponse getEndUserResponseById(long userId) {
        log.info("Fetching end user by ID: {}", userId);
        GetEndUserResponse response = new GetEndUserResponse();
        EndUserProjection endUserProjection = endUserRepository.findEndUserById(userId);
        response.setEndUserProjection(endUserProjection);
        return response;
    }

    public EndUser getEndUserById(Long userId) {
        log.debug("Retrieving full details for end user ID: {}", userId);
        return  endUserRepository.findById(userId).get();
    }
}
