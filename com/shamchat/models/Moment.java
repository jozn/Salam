package com.shamchat.models;

import java.util.List;

public final class Moment {
    public int commentCount;
    public List<String> imgUrls;
    public int likeCount;
    public boolean likedByCurrentUser;
    public String momentId;
    public String postedDateTime;
    public String postedLocation;
    public String postedText;
    public String postedVideoUrl;
    public String serverPostId;
    public List<String> stickerUrls;
    public String userId;
    public String userName;

    public Moment() {
        this.likedByCurrentUser = false;
    }
}
