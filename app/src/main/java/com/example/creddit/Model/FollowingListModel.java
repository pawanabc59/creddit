package com.example.creddit.Model;

import android.widget.ImageView;
import android.widget.TextView;

public class FollowingListModel {

    public int sub_image;
    public String sub_name;

    public FollowingListModel(int sub_image, String sub_name) {
        this.sub_image = sub_image;
        this.sub_name = sub_name;
    }

    public int getSub_image() {
        return sub_image;
    }

    public void setSub_image(int sub_image) {
        this.sub_image = sub_image;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }
}
