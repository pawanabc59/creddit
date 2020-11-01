package com.example.creddit.RoomDatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HistoryPosts {
    @NonNull
    @PrimaryKey
    String r_id;
    String r_cardPostProfileImage;
    String r_imagePath;
    String r_subName;
    String r_uploadedBy;
    String r_cardTitle;
    String r_cardPostTime;
    String r_userId;
    int r_NSFW;
    int r_spoiler;
    String r_postType;
    String r_subId;
    String r_subType;
    String r_currentUser;

    public HistoryPosts(String r_id, String r_cardPostProfileImage, String r_imagePath, String r_subName, String r_uploadedBy, String r_cardTitle, String r_cardPostTime, String r_userId, int r_NSFW, int r_spoiler, String r_postType, String r_subId, String r_subType, String r_currentUser) {
        this.r_id = r_id;
        this.r_cardPostProfileImage = r_cardPostProfileImage;
        this.r_imagePath = r_imagePath;
        this.r_subName = r_subName;
        this.r_uploadedBy = r_uploadedBy;
        this.r_cardTitle = r_cardTitle;
        this.r_cardPostTime = r_cardPostTime;
        this.r_userId = r_userId;
        this.r_NSFW = r_NSFW;
        this.r_spoiler = r_spoiler;
        this.r_postType = r_postType;
        this.r_subId = r_subId;
        this.r_subType = r_subType;
        this.r_currentUser = r_currentUser;
    }

    @NonNull
    public String getR_id() {
        return r_id;
    }

    public void setR_id(@NonNull String r_id) {
        this.r_id = r_id;
    }

    public String getR_cardPostProfileImage() {
        return r_cardPostProfileImage;
    }

    public void setR_cardPostProfileImage(String r_cardPostProfileImage) {
        this.r_cardPostProfileImage = r_cardPostProfileImage;
    }

    public String getR_imagePath() {
        return r_imagePath;
    }

    public void setR_imagePath(String r_imagePath) {
        this.r_imagePath = r_imagePath;
    }

    public String getR_subName() {
        return r_subName;
    }

    public void setR_subName(String r_subName) {
        this.r_subName = r_subName;
    }

    public String getR_uploadedBy() {
        return r_uploadedBy;
    }

    public void setR_uploadedBy(String r_uploadedBy) {
        this.r_uploadedBy = r_uploadedBy;
    }

    public String getR_cardTitle() {
        return r_cardTitle;
    }

    public void setR_cardTitle(String r_cardTitle) {
        this.r_cardTitle = r_cardTitle;
    }

    public String getR_cardPostTime() {
        return r_cardPostTime;
    }

    public void setR_cardPostTime(String r_cardPostTime) {
        this.r_cardPostTime = r_cardPostTime;
    }

    public String getR_userId() {
        return r_userId;
    }

    public void setR_userId(String r_userId) {
        this.r_userId = r_userId;
    }

    public int getR_NSFW() {
        return r_NSFW;
    }

    public void setR_NSFW(int r_NSFW) {
        this.r_NSFW = r_NSFW;
    }

    public int getR_spoiler() {
        return r_spoiler;
    }

    public void setR_spoiler(int r_spoiler) {
        this.r_spoiler = r_spoiler;
    }

    public String getR_postType() {
        return r_postType;
    }

    public void setR_postType(String r_postType) {
        this.r_postType = r_postType;
    }

    public String getR_subId() {
        return r_subId;
    }

    public void setR_subId(String r_subId) {
        this.r_subId = r_subId;
    }

    public String getR_subType() {
        return r_subType;
    }

    public void setR_subType(String r_subType) {
        this.r_subType = r_subType;
    }

    public String getR_currentUser() {
        return r_currentUser;
    }

    public void setR_currentUser(String r_currentUser) {
        this.r_currentUser = r_currentUser;
    }
}
