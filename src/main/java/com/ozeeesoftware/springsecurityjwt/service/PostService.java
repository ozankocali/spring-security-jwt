package com.ozeeesoftware.springsecurityjwt.service;

import com.ozeeesoftware.springsecurityjwt.model.Post;
import com.ozeeesoftware.springsecurityjwt.constant.PostStatus;
import com.ozeeesoftware.springsecurityjwt.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public ResponseEntity<Post> createPost(Post post, Principal principal){
        post.setPostStatus(PostStatus.PENDING);
        post.setPostUserName(principal.getName());

        //return principal.getName()+"your post published successfully!";
        return new ResponseEntity<Post>(postRepository.save(post), HttpStatus.OK);
    }

    public ResponseEntity<Post> approvePost(long postId){

        Post existingPost= postRepository.findById(postId).get();
        existingPost.setPostStatus(PostStatus.APPROVED);

        //return "Post approved";
        return new ResponseEntity<Post>(postRepository.save(existingPost),HttpStatus.OK);

    }

    public ResponseEntity<List<Post>> approveAll(){

        List<Post> postList=new ArrayList<>();

        postRepository.findAll().stream().filter(post -> post.getPostStatus().equals(PostStatus.PENDING))
                .forEach(post -> {
                    post.setPostStatus(PostStatus.APPROVED);
                    postRepository.save(post);
                    postList.add(post);
                });

        //return "All posts are approved";
        return new ResponseEntity<List<Post>>(postList,HttpStatus.OK);
    }

    public ResponseEntity<Post> rejectPost(long postId){

        Post existingPost= postRepository.findById(postId).get();
        existingPost.setPostStatus(PostStatus.REJECTED);

        //return "Post rejected";
        return new ResponseEntity<Post>(postRepository.save(existingPost),HttpStatus.OK);

    }

    public ResponseEntity<List<Post>> rejectAll(){

        List<Post> postList=new ArrayList<>();

        postRepository.findAll().stream().filter(post -> post.getPostStatus().equals(PostStatus.PENDING))
                .forEach(post -> {
                    post.setPostStatus(PostStatus.REJECTED);
                    postRepository.save(post);
                    postList.add(post);
                });

        //return "All posts are rejected";
        return new ResponseEntity<List<Post>>(postList,HttpStatus.OK);
    }

    public ResponseEntity<List<Post>> posts(){
        return new ResponseEntity<List<Post>>(postRepository.findAll().stream().filter(post -> post.getPostStatus().equals(PostStatus.APPROVED)).collect(Collectors.toList()),HttpStatus.OK);
    }

}
