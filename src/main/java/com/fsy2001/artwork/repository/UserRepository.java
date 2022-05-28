package com.fsy2001.artwork.repository;

import com.fsy2001.artwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("select u from User u where u.username = :username")
    User getUserByUsername(@Param("username") String username);

    @Query("select u from User u where u.email = :email")
    User getUserByEmail(@Param("email") String email);
}