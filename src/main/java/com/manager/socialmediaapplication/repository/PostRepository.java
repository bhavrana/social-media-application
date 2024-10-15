package com.manager.socialmediaapplication.repository;

import com.manager.socialmediaapplication.model.Post;
import com.manager.socialmediaapplication.model.projection.PostProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Transactional
    @Query(value =
            "UPDATE post p set p.content = :updatedContent WHERE p.id = :postId",
            nativeQuery=true)
    void updatePostById(@Param("postId") Long postId, @Param("updatedContent") String updatedContent);

    @Query(value =
            "SELECT p.id AS id, p.content AS content, p.created_date AS createdDate, u.name AS endUserName, COALESCE(pr.like_count, 0) AS likeCount, COALESCE(pr.dislike_count, 0) AS dislikeCount FROM post p " +
                    "LEFT JOIN end_user u ON p.end_user_id = u.id " +
                    "LEFT JOIN post_reaction pr ON pr.post_id = p.id " +
                    "WHERE p.id = :post_id",
            nativeQuery=true)
    PostProjection findPostByPostId(@Param("post_id") long postId);

    @Query(value =
            "SELECT p.id AS id, p.content AS content, p.created_date AS createdDate, u.name AS endUserName, COALESCE(pr.like_count, 0) AS likeCount, COALESCE(pr.dislike_count, 0) AS dislikeCount FROM post p " +
                    "LEFT JOIN end_user u ON p.end_user_id = u.id " +
                    "LEFT JOIN post_reaction pr ON pr.post_id = p.id",
            nativeQuery=true)
    Page<PostProjection> getPosts(Pageable pageable);

    @Query(value =
            "SELECT p.id AS id, p.content AS content, p.created_date AS createdDate, u.name AS endUserName, COALESCE(pr.like_count, 0) AS likeCount, COALESCE(pr.dislike_count, 0) AS dislikeCount FROM post p " +
                    "LEFT JOIN end_user u ON p.end_user_id = u.id " +
                    "LEFT JOIN post_reaction pr ON pr.post_id = p.id " +
                    "WHERE u.id = :user_id ",
            nativeQuery=true)
    Page<PostProjection> getPostForUserId(@Param("user_id") long userId, Pageable pageable);
}
