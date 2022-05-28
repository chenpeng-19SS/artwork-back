package com.fsy2001.artwork.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    private String id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Artwork artwork;

    public Cart() {
    }

    public Cart(User user, Artwork artwork) {
        this.user = user;
        this.artwork = artwork;
        id = user.getUsername() + "-" + artwork.getId().toString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }
}
