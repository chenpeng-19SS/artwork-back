package com.fsy2001.artwork.model.support;

public class FriendDisplay {
    private String username;
    private String img;
    private String recentMessage;

    public FriendDisplay(String username, String img, String recentMessage) {
        this.username = username;
        this.img = img;
        this.recentMessage = recentMessage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRecentMessage() {
        return recentMessage;
    }

    public void setRecentMessage(String recentMessage) {
        this.recentMessage = recentMessage;
    }
}
