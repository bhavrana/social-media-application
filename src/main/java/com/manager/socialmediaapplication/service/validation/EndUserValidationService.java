package com.manager.socialmediaapplication.service.validation;

import com.manager.socialmediaapplication.repository.EndUserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EndUserValidationService {
    EndUserRepository endUserRepository;

    @Autowired
    public EndUserValidationService(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
    }

    public boolean doesUserExist(long userId) {
        return endUserRepository.existsById(userId);
    }
}
