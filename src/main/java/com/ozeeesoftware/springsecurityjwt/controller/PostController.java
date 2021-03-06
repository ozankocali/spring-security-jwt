package com.ozeeesoftware.springsecurityjwt.controller;

import com.ozeeesoftware.springsecurityjwt.model.Post;
import com.ozeeesoftware.springsecurityjwt.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/createPost")
    public ResponseEntity<Post> createPost(@RequestBody Post post, Principal principal){
        return postService.createPost(post,principal);
    }

    @GetMapping("/approve/{postId}")
    @PreAuthorize("hasAnyAuthority('post:approve')")
    public ResponseEntity<Post> approvePost(@PathVariable int postId){
        return postService.approvePost(postId);
    }

    @GetMapping("/approveAll")
    @PreAuthorize("hasAnyAuthority('post:approve')")
    public ResponseEntity<List<Post>> approveAll(){
        return postService.approveAll();
    }

    @GetMapping("/reject")
    @PreAuthorize("hasAnyAuthority('post:approve')")
    public ResponseEntity<Post> rejectPost(long postId){
        return postService.rejectPost(postId);
    }

    @GetMapping("/rejectAll")
    @PreAuthorize("hasAnyAuthority('post:approve')")
    public ResponseEntity<List<Post>> rejectAll(){
        return postService.rejectAll();
    }

    @GetMapping("/posts")
    public List<Post> posts(){
        return (List<Post>) postService.posts();
    }

}
