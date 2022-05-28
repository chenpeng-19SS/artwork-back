package com.fsy2001.artwork.repository;

import com.fsy2001.artwork.model.Artwork;
import com.fsy2001.artwork.model.Cart;
import com.fsy2001.artwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    List<Cart> findByUser(User user);
    Cart findByUserAndArtwork(User user, Artwork artwork);
    void deleteByUser(User user);
}
