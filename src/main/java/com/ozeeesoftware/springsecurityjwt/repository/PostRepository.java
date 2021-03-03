package com.ozeeesoftware.springsecurityjwt.repository;

import com.ozeeesoftware.springsecurityjwt.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
