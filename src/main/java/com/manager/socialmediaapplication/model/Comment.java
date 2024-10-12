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
public class Comment extends BaseEntityExtension {
    @Column(nullable = false, columnDefinition = "text", length = 2500)
    String content;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "end_user_id", referencedColumnName = "id")
    EndUser endUser;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    Post post;

    @OneToOne(mappedBy = "comment") //-----
    CommentReaction commentReaction;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    Comment parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL)
    private List<Comment> children;

    @OneToMany(mappedBy = "comment")
    Set<CommentUserInteraction> commentUserInteractions;
}
