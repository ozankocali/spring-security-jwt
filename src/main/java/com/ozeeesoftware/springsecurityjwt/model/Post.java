package com.ozeeesoftware.springsecurityjwt.model;

import com.ozeeesoftware.springsecurityjwt.constant.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String subject;
    private String description;
    private String postUserName;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;



}
