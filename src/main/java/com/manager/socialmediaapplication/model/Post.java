package com.manager.socialmediaapplication.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Post extends BaseEntityExtension {
    @Column(nullable = false, length = 2500)
    String content;

    @ManyToOne
    @JoinColumn(name = "end_user_id", referencedColumnName = "id")
    EndUser endUser;

    @OneToMany(mappedBy = "post")
    List<Comment> comments;

    @OneToOne(mappedBy = "post")
    PostReaction postReaction;

    @OneToMany(mappedBy = "post")
    Set<PostUserInteraction> postUserInteractions;
}
