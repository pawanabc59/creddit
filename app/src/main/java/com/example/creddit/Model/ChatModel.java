package com.example.creddit.Model;

public class ChatModel {

    String userImage, chatUserName, chatMessage;

    public ChatModel(String userImage, String chatUserName, String chatMessage) {
        this.userImage = userImage;
        this.chatUserName = chatUserName;
        this.chatMessage = chatMessage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getChatUserName() {
        return chatUserName;
    }

    public void setChatUserName(String chatUserName) {
        this.chatUserName = chatUserName;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }
}
