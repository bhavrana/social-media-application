package com.manager.socialmediaapplication.validation;

import com.manager.socialmediaapplication.exception.UserNotFoundException;
import com.manager.socialmediaapplication.repository.EndUserRepository;
import com.manager.socialmediaapplication.service.validation.EndUserValidationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EndUserValidationServiceTest {
    @InjectMocks
    EndUserValidationService endUserValidationService;

    @Mock
    EndUserRepository endUserRepository;

    @Test
    void test_doesUserExist_UserExists() {
        long userId = 1L;
        when(endUserRepository.existsById(userId)).thenReturn(true);
        endUserValidationService.doesUserExist(userId);
    }

    @Test
    void test_doesUserExist_UserDoesNotExist() {
        long userId = 1L;
        when(endUserRepository.existsById(userId)).thenReturn(false);
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> endUserValidationService.doesUserExist(userId));
        String expectedMessage = "User not found for Id " + userId;
        assert expectedMessage.equals(exception.getMessage());
    }
}
