package com.manager.socialmediaapplication.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "end_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class EndUser extends BaseEntityExtension{
    @Column(nullable = false)
    String name;

    @Column(nullable = false, unique = true)
    String email;

    @OneToMany(mappedBy = "endUser")
    List<Comment> comments;

    @OneToMany(mappedBy = "endUser")
    List<Post> posts;

    @OneToMany(mappedBy = "endUser")
    Set<CommentUserInteraction> commentUserInteractions;

    //@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    //Set<PostUserInteraction> postUserInteractions;
}
