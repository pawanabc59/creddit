package com.example.creddit.Model;

import android.widget.ImageView;
import android.widget.TextView;

public class FollowingListModel {

    public String sub_name, sub_image, anotherUserId, type;

    public FollowingListModel(String sub_image, String sub_name, String anotherUserId, String type) {
        this.sub_image = sub_image;
        this.sub_name = sub_name;
        this.anotherUserId = anotherUserId;
        this.type = type;
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
}
