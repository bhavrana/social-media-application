package com.manager.socialmediaapplication.service;

import com.manager.socialmediaapplication.dto.request.EndUserCreationRequest;
import com.manager.socialmediaapplication.dto.response.GetEndUserResponse;
import com.manager.socialmediaapplication.dto.response.GetEndUsersResponse;
import com.manager.socialmediaapplication.exception.UserCreationException;
import com.manager.socialmediaapplication.model.EndUser;
import com.manager.socialmediaapplication.model.projection.EndUserProjection;
import com.manager.socialmediaapplication.repository.EndUserRepository;
import com.manager.socialmediaapplication.service.implementation.EndUserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EndUserServiceTest {
    @InjectMocks
    EndUserService endUserService;

    @Mock
    EndUserRepository endUserRepository;

    @Test
    void test_createEndUser_Success() {
        EndUserCreationRequest endUserCreationRequest = new EndUserCreationRequest();
        EndUserProjection endUserProjection = new EndUserProjection() {
            @Override
            public String getId() {
                return "1";
            }

            @Override
            public String getName() {
                return "Test User";
            }

            @Override
            public String getEmail() {
                return "test@example.com";
            }
        };
        endUserCreationRequest.setEmail("test@example.com");
        endUserCreationRequest.setName("Test User");
        EndUser endUser = new EndUser();
        endUser.setId(1L);
        endUser.setEmail("test@example.com");
        endUser.setName("Test User");
        when(endUserRepository.save(any(EndUser.class))).thenReturn(endUser);
        endUserRepository.save(endUser);
        when(endUserRepository.findEndUserById(anyLong())).thenReturn(endUserProjection);
        GetEndUserResponse response = endUserService.createEndUser(endUserCreationRequest);
        assertNotNull(response);
        assertEquals(endUserProjection, response.getEndUserProjection());
    }

    @Test
    void test_createEndUser_Failure() {
        EndUserCreationRequest endUserCreationRequest = new EndUserCreationRequest();
        endUserCreationRequest.setEmail("test@example.com");
        endUserCreationRequest.setName("Test User");
        when(endUserRepository.save(ArgumentMatchers.any(EndUser.class))).thenThrow(new RuntimeException("Test exception"));
        UserCreationException exception = assertThrows(UserCreationException.class, () -> endUserService.createEndUser(endUserCreationRequest));
        assertNotNull(exception);
        assertEquals("Test exception", exception.getMessage());
    }

    @Test
    void test_getEndUsers_Ascending() {
        Integer pageNo = 0;
        Integer pageSize = 10;
        String orderBy = "ASC";
        Sort sort = Sort.by("CREATED_DATE").ascending();
        Page<EndUserProjection> endUsers = Page.empty();
        when(endUserRepository.findAllProjectedBy(PageRequest.of(pageNo, pageSize, sort))).thenReturn(endUsers);
        GetEndUsersResponse response = endUserService.getEndUsers(pageNo, pageSize, orderBy);
        assertNotNull(response);
        assertEquals(endUsers, response.getEndUserList());
    }

    @Test
    void testG_getEndUsers_Descending() {
        Integer pageNo = 0;
        Integer pageSize = 10;
        String orderBy = "DESC";
        Sort sort = Sort.by("CREATED_DATE").descending();
        Page<EndUserProjection> endUsers = Page.empty();
        when(endUserRepository.findAllProjectedBy(PageRequest.of(pageNo, pageSize, sort))).thenReturn(endUsers);
        GetEndUsersResponse response = endUserService.getEndUsers(pageNo, pageSize, orderBy);
        assertNotNull(response);
        assertEquals(endUsers, response.getEndUserList());
    }

    @Test
    void test_getEndUserResponseById_Success() {
        long userId = 1L;
        EndUserProjection endUserProjection = new EndUserProjection() {
            @Override
            public String getId() {
                return "1";
            }

            @Override
            public String getName() {
                return "Test User";
            }

            @Override
            public String getEmail() {
                return "test@example.com";
            }
        };
        when(endUserRepository.findEndUserById(userId)).thenReturn(endUserProjection);
        GetEndUserResponse response = endUserService.getEndUserResponseById(userId);
        assertNotNull(response);
        assertEquals(endUserProjection, response.getEndUserProjection());
    }
}
