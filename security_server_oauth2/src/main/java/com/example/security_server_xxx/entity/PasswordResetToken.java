package com.example.security_server_xxx.entity;

import javax.persistence.*;


@Entity
public class PasswordResetToken {

    private static final int EXPIRATION_IN_MILISEC = 1000 * 60 * 60 * 12;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users users;

    private long expiryDate;

    public PasswordResetToken() {}

    public PasswordResetToken(String token, Users users) {
        this.expiryDate = System.currentTimeMillis();
        this.token = token;
        this.users = users;
    }


    public Long getId() {
        return id;
    }


    public Users getUsers() {
        return users;
    }

    public String getToken() {
        return token;
    }


    public long getExpiryDate() {
        return expiryDate;
    }

    public static int getEXPIRATION() {
        return EXPIRATION_IN_MILISEC;
    }
}
