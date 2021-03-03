package com.ozeeesoftware.springsecurityjwt.service;

import com.ozeeesoftware.springsecurityjwt.model.Post;
import com.ozeeesoftware.springsecurityjwt.model.PostStatus;
import com.ozeeesoftware.springsecurityjwt.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public String createPost(Post post, Principal principal){
        post.setPostStatus(PostStatus.PENDING);
        post.setPostUserName(principal.getName());
        postRepository.save(post);

        return principal.getName()+"your post published successfully!";
    }

    public String approvePost(long postId){

        Post existingPost= postRepository.findById(postId).get();
        existingPost.setPostStatus(PostStatus.APPROVED);
        postRepository.save(existingPost);

        return "Post approved";

    }

    public String approveAll(){

        postRepository.findAll().stream().filter(post -> post.getPostStatus().equals(PostStatus.PENDING))
                .forEach(post -> {
                    post.setPostStatus(PostStatus.APPROVED);
                    postRepository.save(post);
                });

        return "All posts are approved";
    }

    public String rejectPost(long postId){

        Post existingPost= postRepository.findById(postId).get();
        existingPost.setPostStatus(PostStatus.REJECTED);
        postRepository.save(existingPost);

        return "Post rejected";

    }

    public String rejectAll(){

        postRepository.findAll().stream().filter(post -> post.getPostStatus().equals(PostStatus.PENDING))
                .forEach(post -> {
                    post.setPostStatus(PostStatus.REJECTED);
                    postRepository.save(post);
                });

        return "All posts are rejected";
    }

    public List<Post> posts(){
        return postRepository.findAll().stream().filter(post -> post.getPostStatus().equals(PostStatus.APPROVED)).collect(Collectors.toList());
    }

}
