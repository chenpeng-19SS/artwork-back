package com.fsy2001.artwork.repository;

import com.fsy2001.artwork.model.BrowseHistory;
import com.fsy2001.artwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrowseHistoryRepository extends JpaRepository<BrowseHistory, Integer> {
    List<BrowseHistory> findByUserOrderByDate(User user);
}
