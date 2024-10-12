package com.manager.socialmediaapplication.service.implementation;

import com.manager.socialmediaapplication.dto.request.EndUserCreationRequest;
import com.manager.socialmediaapplication.dto.response.GetEndUserResponse;
import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;
import com.manager.socialmediaapplication.exception.UserCreationException;
import com.manager.socialmediaapplication.exception.UserNotFoundException;
import com.manager.socialmediaapplication.model.EndUser;
import com.manager.socialmediaapplication.model.projection.EndUserProjection;
import com.manager.socialmediaapplication.repository.EndUserRepository;
import com.manager.socialmediaapplication.service.view.EndUserServiceView;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EndEndUserService implements EndUserServiceView {

    EndUserRepository endUserRepository;

    @Autowired
    public EndEndUserService(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
    }

    @Override
    public void createEndUser(EndUserCreationRequest endUserCreationRequest) {
        EndUser endUser = new EndUser();
        endUser.setEmail(endUserCreationRequest.getEmail());
        endUser.setName(endUserCreationRequest.getName());
        try {
            endUserRepository.save(endUser);
        } catch (Exception e) {
            log.error("Error creating end user", e);
            throw new UserCreationException(e.getMessage());
        }
    }

    @Override
    public GetEndUsersResponse getEndUsers() {
        GetEndUsersResponse response = new GetEndUsersResponse();
        List<EndUserProjection> endUsers = endUserRepository.findAllProjectedBy();
        response.setEndUserList(endUsers);
        return response;
    }

    @Override
    public GetEndUserResponse getEndUserById(long userId) {
        GetEndUserResponse response = new GetEndUserResponse();
        EndUserProjection endUserProjection = endUserRepository.findEndUserById(userId);
        response.setEndUserProjection(endUserProjection);
        return response;
    }

    @Override
    public void deleteEndUserById(long userId) {
        if (!endUserRepository.existsById(userId)) {
            //Throw exception
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        endUserRepository.deleteById(userId);
    }
}
