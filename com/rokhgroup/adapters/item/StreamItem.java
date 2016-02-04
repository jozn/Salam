package com.rokhgroup.adapters.item;

public final class StreamItem {
    public String IMAGE_THUMB_URL;
    public String POST_CMNT_CNT;
    public String POST_DATE;
    public String POST_ID;
    public String POST_LIKE_CNT;
    public String POST_LIKE_W_USER;
    private String POST_TEXT;
    public String POST_TYPE;
    public String USER_AVATAR;
    public String USER_ID;
    public String USER_NAME;

    public StreamItem(String postId, String thumbnailUrl, String userId, String userName, String userAvatar, String postDate, String postLikeCnt, String postLikeWUser, String postCmntCnt, String postText, String postType) {
        this.POST_ID = postId;
        this.IMAGE_THUMB_URL = thumbnailUrl;
        this.USER_ID = userId;
        this.USER_NAME = userName;
        this.USER_AVATAR = userAvatar;
        this.POST_DATE = postDate;
        this.POST_LIKE_CNT = postLikeCnt;
        this.POST_LIKE_W_USER = postLikeWUser;
        this.POST_CMNT_CNT = postCmntCnt;
        this.POST_TEXT = postText;
        this.POST_TYPE = postType;
    }
}
