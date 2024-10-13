package com.manager.socialmediaapplication.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class PostUserInteraction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "end_user_id", referencedColumnName = "id")
    EndUser endUser;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    Post post;

    @Column(name = "is_liked", columnDefinition = "boolean", nullable = false)
    Boolean isLiked;

    @Column(name = "is_active", columnDefinition = "boolean", nullable = false)
    @ColumnDefault(value = "true")
    Boolean isActive;
}
