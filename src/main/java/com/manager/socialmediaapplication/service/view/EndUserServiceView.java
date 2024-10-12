package com.manager.socialmediaapplication.service.view;

import com.manager.socialmediaapplication.dto.request.EndUserCreationRequest;
import com.manager.socialmediaapplication.dto.response.GetEndUserResponse;
import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;

public interface EndUserServiceView {
    void createEndUser(EndUserCreationRequest endUserCreationRequest);
    GetEndUsersResponse getEndUsers();

    GetEndUserResponse getEndUserById(long userId);

    void deleteEndUserById(long userId);
}
