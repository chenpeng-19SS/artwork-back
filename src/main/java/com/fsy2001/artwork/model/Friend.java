package com.fsy2001.artwork.model;

import javax.persistence.*;

@Entity
@Table(name = "friend")
public class Friend { // TODO: ID 改为 String 以优化
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String self;
    private String friend;
    private boolean approved = false;

    public Friend() {
    }

    public Friend(String self, String friend, boolean approved) {
        this.self = self;
        this.friend = friend;
        this.approved = approved;
    }



    public Integer getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
