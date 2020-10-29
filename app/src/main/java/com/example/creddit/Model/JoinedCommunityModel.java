package com.example.creddit.Model;

public class JoinedCommunityModel {

    public String sub_name, sub_image, anotherUserId, subType, type, feedName, members;

//    anotherUserId is also subreddit id
    public JoinedCommunityModel(String sub_image, String sub_name, String anotherUserId, String subType, String type, String feedName, String members) {
        this.sub_image = sub_image;
        this.sub_name = sub_name;
        this.anotherUserId = anotherUserId;
        this.type = type;
        this.subType = subType;
        this.feedName = feedName;
        this.members = members;
    }

    public String getSub_image() {
        return sub_image;
    }

    public void setSub_image(String sub_image) {
        this.sub_image = sub_image;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getAnotherUserId() {
        return anotherUserId;
    }

    public void setAnotherUserId(String anotherUserId) {
        this.anotherUserId = anotherUserId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }
}
