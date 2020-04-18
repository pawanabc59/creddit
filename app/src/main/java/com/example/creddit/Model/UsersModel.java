package com.example.creddit.Model;

import android.widget.ImageView;

public class UsersModel {

    String UserName, UserProfileImage, ReceiverId;

    public UsersModel(String userName, String userProfileImage, String receiverId) {
        UserName = userName;
        UserProfileImage = userProfileImage;
        ReceiverId = receiverId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserProfileImage() {
        return UserProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        UserProfileImage = userProfileImage;
    }

    public String getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(String receiverId) {
        ReceiverId = receiverId;
    }
}
