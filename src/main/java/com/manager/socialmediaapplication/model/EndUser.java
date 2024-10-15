package com.manager.socialmediaapplication.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "end_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EndUser extends BaseEntityExtension {
    @Column(nullable = false)
    String name;

    @Column(nullable = false, unique = true)
    String email;

    @OneToMany(mappedBy = "endUser", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> comments;

    @OneToMany(mappedBy = "endUser", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Post> posts;

    @OneToMany(mappedBy = "endUser", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CommentUserInteraction> commentUserInteractions;

    @OneToMany(mappedBy = "endUser", cascade = CascadeType.ALL, orphanRemoval = true)
    List<PostUserInteraction> postUserInteractions;
}
