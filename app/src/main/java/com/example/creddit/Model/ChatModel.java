package com.example.creddit.Model;

public class ChatModel {

    String userImage, chatUserName, chatMessage, userId, chatId, senderId, senderProfilePicture, senderUserName;

    public ChatModel(String userImage, String chatUserName, String chatMessage, String userId, String chatId) {
        this.userImage = userImage;
        this.chatUserName = chatUserName;
        this.chatMessage = chatMessage;
        this.userId = userId;
        this.chatId = chatId;
//        this.receiverId = receiverId;
//        this.senderId = senderId;
//        this.senderProfilePicture = senderProfilePicture;
//        this.senderUserName = senderUserName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    //    public String getReceiverId() {
//        return receiverId;
//    }
//
//    public void setReceiverId(String receiverId) {
//        this.receiverId = receiverId;
//    }
//
//    public String getSenderId() {
//        return senderId;
//    }
//
//    public void setSenderId(String senderId) {
//        this.senderId = senderId;
//    }
//
//    public String getSenderProfilePicture() {
//        return senderProfilePicture;
//    }
//
//    public void setSenderProfilePicture(String senderProfilePicture) {
//        this.senderProfilePicture = senderProfilePicture;
//    }
//
//    public String getSenderUserName() {
//        return senderUserName;
//    }
//
//    public void setSenderUserName(String senderUserName) {
//        this.senderUserName = senderUserName;
//    }
}
