package com.ozeeesoftware.springsecurityjwt.repository;

import com.ozeeesoftware.springsecurityjwt.model.Role;
import com.ozeeesoftware.springsecurityjwt.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleEnum name);
}
