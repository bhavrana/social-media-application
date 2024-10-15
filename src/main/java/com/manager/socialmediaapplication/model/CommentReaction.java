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
public class CommentReaction extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    Comment comment;

    @Column(nullable = false)
    @ColumnDefault("0")
    Long likeCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    Long dislikeCount;
}
