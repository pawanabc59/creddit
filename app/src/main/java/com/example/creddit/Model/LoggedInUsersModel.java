package com.example.creddit.Model;

public class LoggedInUsersModel {
    String userName, userPhoto, userIds, usersPassword, userEmail;

    public LoggedInUsersModel(String userName, String userPhoto, String userIds, String usersPassword, String userEmail) {
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.userIds = userIds;
        this.usersPassword = usersPassword;
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public String getUsersPassword() {
        return usersPassword;
    }

    public void setUsersPassword(String usersPassword) {
        this.usersPassword = usersPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
