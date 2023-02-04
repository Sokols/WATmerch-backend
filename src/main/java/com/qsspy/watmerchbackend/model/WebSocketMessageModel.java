package com.qsspy.watmerchbackend.model;


import java.util.UUID;

public class WebSocketMessageModel {

    private UUID senderId;
    private String name;
    private String message;

    public WebSocketMessageModel(UUID senderId, String name, String message) {
        this.senderId = senderId;
        this.name = name;
        this.message = message;
    }

    public WebSocketMessageModel(){}

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
