package com.example.creddit.Model;

import android.widget.ImageView;
import android.widget.TextView;

public class FollowingListModel {

    public String sub_name, sub_image, anotherUserId;

    public FollowingListModel(String sub_image, String sub_name, String anotherUserId) {
        this.sub_image = sub_image;
        this.sub_name = sub_name;
        this.anotherUserId = anotherUserId;
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
}
