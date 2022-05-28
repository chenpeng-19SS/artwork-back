package com.fsy2001.artwork.repository;

import com.fsy2001.artwork.model.Message;
import com.fsy2001.artwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByFromAndTo(User from, User to);
}