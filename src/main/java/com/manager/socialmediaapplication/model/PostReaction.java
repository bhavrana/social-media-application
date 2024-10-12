package com.manager.socialmediaapplication.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostReaction extends BaseEntity {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    Post post;

    @Column(nullable = false, name = "like_count")
    @ColumnDefault("0")
    Long likeCount;

    @Column(nullable = false, name = "dislike_count")
    @ColumnDefault("0")
    Long dislikeCount;
}
