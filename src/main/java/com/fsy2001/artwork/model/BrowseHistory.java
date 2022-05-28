package com.fsy2001.artwork.model;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "browse_history")
public class BrowseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Artwork artwork;
    private Date date;

    public BrowseHistory(User user, Artwork artwork) {
        this.user = user;
        this.artwork = artwork;
        this.date = new Date();
    }

    public BrowseHistory() {
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BrowseHistory)) return false;

        BrowseHistory history = (BrowseHistory) obj;
        return history.getArtwork().getId().equals(this.artwork.getId())
                && history.getUser().getUsername().equals(this.user.getUsername());
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
