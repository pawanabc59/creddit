package com.example.creddit.Model;

import android.widget.ImageView;

public class UsersModel {

    String UserName, UserProfileImage, ReceiverId;
    int ReceiverNumber;

    public UsersModel(String userName, String userProfileImage, String receiverId, int receiverNumber) {
        UserName = userName;
        UserProfileImage = userProfileImage;
        ReceiverId = receiverId;
        ReceiverNumber = receiverNumber;
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

    public int getReceiverNumber() {
        return ReceiverNumber;
    }

    public void setReceiverNumber(int receiverNumber) {
        ReceiverNumber = receiverNumber;
    }
}
