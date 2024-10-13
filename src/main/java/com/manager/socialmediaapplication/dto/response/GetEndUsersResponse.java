package com.manager.socialmediaapplication.dto.response;

import com.manager.socialmediaapplication.model.projection.EndUserProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetEndUsersResponse {
    Page<EndUserProjection> endUserList;
}
