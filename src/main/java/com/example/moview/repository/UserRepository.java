package com.example.moview.repository;

import com.example.moview.model.Role;
import com.example.moview.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"roles", "reviews"})
    Optional<User> findById(Long id);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmail(String email);

}