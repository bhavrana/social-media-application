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
        @Index(name = "column-post-index", columnList = "post_id"),
        @Index(name = "column-parent-index", columnList = "parent_id")
})
public class Comment extends BaseEntityExtension {
    @Column(nullable = false, columnDefinition = "text", length = 2500)
    String content;

    @ManyToOne
    @JoinColumn(name = "end_user_id", referencedColumnName = "id")
    EndUser endUser;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    Post post;

    @OneToOne(mappedBy = "comment")
    CommentReaction commentReaction;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    Comment parent;

    @OneToMany(mappedBy="parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CommentUserInteraction> commentUserInteractions;
}
