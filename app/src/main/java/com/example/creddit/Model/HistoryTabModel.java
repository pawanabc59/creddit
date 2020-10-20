package com.example.creddit.Model;

public class HistoryTabModel {
    String key, name, profilePicture, type;

    public HistoryTabModel(String key, String name, String profilePicture, String type) {
        this.key = key;
        this.name = name;
        this.profilePicture = profilePicture;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
