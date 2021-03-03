package com.ozeeesoftware.springsecurityjwt.controller;

import com.ozeeesoftware.springsecurityjwt.model.Post;
import com.ozeeesoftware.springsecurityjwt.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String createPost(@RequestBody Post post, Principal principal){
        return postService.createPost(post,principal);
    }

    @GetMapping("/approve/{postId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public String approvePost(@PathVariable int postId){
        return postService.approvePost(postId);
    }

    @GetMapping("/approveAll")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public String approveAll(){
        return postService.approveAll();
    }

    @GetMapping("/reject")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public String rejectPost(long postId){
        return postService.rejectPost(postId);
    }

    @GetMapping("/rejectAll")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public String rejectAll(){
        return postService.rejectAll();
    }

    @GetMapping("/posts")
    public List<Post> posts(){
        return postService.posts();
    }

}
