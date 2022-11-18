package com.nhnacademy.springmvc.domain;

import lombok.Getter;

@Getter
public class Admin {
    private final String id;
    private final String password;

    public Admin() {
        this.id = "admin";
        this.password = "12345";
    }
}
