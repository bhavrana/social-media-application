package com.manager.socialmediaapplication.service.intrface;

import com.manager.socialmediaapplication.dto.request.EndUserCreationRequest;
import com.manager.socialmediaapplication.dto.response.GetEndUserResponse;
import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;

public interface EndUserServiceInterface {
    GetEndUserResponse createEndUser(EndUserCreationRequest endUserCreationRequest);
    GetEndUsersResponse getEndUsers(Integer pageNo, Integer pageSize, String order);

    GetEndUserResponse getEndUserResponseById(long userId);
}
