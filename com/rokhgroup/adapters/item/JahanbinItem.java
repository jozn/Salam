package com.rokhgroup.adapters.item;

public final class JahanbinItem {
    private String ID;
    public String IMAGE_THUMB_URL;
    private String IMAGE_URL;
    public String POST_ID;
    public String Post_TYPE;
    public String USER_ID;

    public JahanbinItem(String id, String imageUrl, String thumbUrl, String userId, String postId, String postType) {
        this.ID = id;
        this.IMAGE_URL = imageUrl;
        this.IMAGE_THUMB_URL = thumbUrl;
        this.USER_ID = userId;
        this.POST_ID = postId;
        this.Post_TYPE = postType;
    }
}
