package com.rokhgroup.adapters.item;

public final class CommentItem {
    public String BODY;
    public String DATE;
    public String ID;
    public String USER_AVA;
    private String USER_ID;
    public String USER_NAME;

    public CommentItem(String id, String avaUrl, String userName, String userId, String body, String date) {
        this.ID = id;
        this.USER_AVA = avaUrl;
        this.USER_NAME = userName;
        this.USER_ID = userId;
        this.BODY = body;
        this.DATE = date;
    }
}
