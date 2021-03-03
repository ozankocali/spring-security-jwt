package com.ozeeesoftware.springsecurityjwt.model;

import javax.persistence.*;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;
    private String subject;
    private String description;
    private String postUserName;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    public Post() {
    }

    public Post(long postId, String subject, String description, String postUserName, PostStatus postStatus) {
        this.postId = postId;
        this.subject = subject;
        this.description = description;
        this.postUserName = postUserName;
        this.postStatus = postStatus;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPostUserName() {
        return postUserName;
    }

    public void setPostUserName(String postUserName) {
        this.postUserName = postUserName;
    }

    public PostStatus getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", postUserName='" + postUserName + '\'' +
                ", postStatus=" + postStatus +
                '}';
    }
}
