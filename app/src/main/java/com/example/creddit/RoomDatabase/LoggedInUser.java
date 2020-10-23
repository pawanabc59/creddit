package com.example.creddit.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LoggedInUser {

    @NonNull
    @PrimaryKey
    String userIds;
    String userEmail;
    String userPassword;
    String userPhoto;
    String userName;

    public LoggedInUser(@NonNull String userIds, String userEmail, String userPassword, String userPhoto, String userName) {
        this.userIds = userIds;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhoto = userPhoto;
        this.userName = userName;
    }

    @NonNull
    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(@NonNull String userIds) {
        this.userIds = userIds;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
