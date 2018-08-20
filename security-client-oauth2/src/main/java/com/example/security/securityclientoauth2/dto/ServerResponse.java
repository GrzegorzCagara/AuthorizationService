package com.example.security.securityclientoauth2.dto;

public class ServerResponse<T> {

    private String status;
    private Long serverTime;
    private String message;
    private T data;


    public ServerResponse(String status, String message) {
        this.serverTime = System.currentTimeMillis();
        this.status = status;
        this.message = message;
    }

    public ServerResponse(String status, String message, T data) {
        this.serverTime = System.currentTimeMillis();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public Long getServerTime() {
        return serverTime;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
