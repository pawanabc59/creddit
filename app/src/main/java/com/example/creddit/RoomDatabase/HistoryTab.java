package com.example.creddit.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HistoryTab {

    @NonNull
    @PrimaryKey
    String subId;
    String subType;
    String profilePicture;
    String subName;
    String userId;

    public HistoryTab(String subId, String subType, String profilePicture, String subName, String userId) {
        this.subId = subId;
        this.subType = subType;
        this.profilePicture = profilePicture;
        this.subName = subName;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
