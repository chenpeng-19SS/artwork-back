package com.fsy2001.artwork.repository;

import com.fsy2001.artwork.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    List<Friend> findBySelfAndApproved(String self, boolean approved);
    Friend findBySelfAndFriend(String self, String friend);
}
