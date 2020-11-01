package com.example.creddit.Model;

public class CardModel {

    public String card_description, postedTime, userId, post_type,posted_by,card_title,card_profile_image, card_image, subId, subType, postId;
    int  nsfw, spoiler;

    public CardModel(String card_profile_image, String card_image, String card_title, String posted_by, String card_description, String postedTime, String userId,
                     int nsfw, int spoiler, String post_type, String subId, String subType, String postId) {
        this.card_profile_image = card_profile_image;
        this.card_image = card_image;
        this.card_title = card_title;
        this.posted_by = posted_by;
        this.card_description = card_description;
        this.postedTime = postedTime;
        this.userId = userId;
        this.nsfw = nsfw;
        this.spoiler = spoiler;
        this.post_type = post_type;
        this.subId = subId;
        this.subType = subType;
        this.postId = postId;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public int getNsfw() {
        return nsfw;
    }

    public void setNsfw(int nsfw) {
        this.nsfw = nsfw;
    }

    public int getSpoiler() {
        return spoiler;
    }

    public void setSpoiler(int spoiler) {
        this.spoiler = spoiler;
    }

    public String getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(String postedTime) {
        this.postedTime = postedTime;
    }

    public String getCard_profile_image() {
        return card_profile_image;
    }

    public void setCard_profile_image(String card_profile_image) {
        this.card_profile_image = card_profile_image;
    }

    public String getCard_image() {
        return card_image;
    }

    public void setCard_image(String card_image) {
        this.card_image = card_image;
    }

    public String getCard_title() {
        return card_title;
    }

    public void setCard_title(String card_title) {
        this.card_title = card_title;
    }

    public String getPosted_by() {
        return posted_by;
    }

    public void setPosted_by(String posted_by) {
        this.posted_by = posted_by;
    }

    public String getCard_description() {
        return card_description;
    }

    public void setCard_description(String card_description) {
        this.card_description = card_description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
