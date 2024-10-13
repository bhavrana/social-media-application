package com.manager.socialmediaapplication.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Table(indexes = {
        @Index(name = "post_user_index", columnList = "end_user_id"),
})
public class Post extends BaseEntityExtension {
    @Column(nullable = false, length = 2500)
    String content;

    @ManyToOne
    @JoinColumn(name = "end_user_id", referencedColumnName = "id")
    EndUser endUser;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> comments;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    PostReaction postReaction;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    List<PostUserInteraction> postUserInteractions;
}
