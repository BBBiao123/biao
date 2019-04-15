package com.biao.util;

import java.time.LocalDateTime;

public class Test {
    private String username;

    private LocalDateTime date;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getDate() {
        return date;
    }

    //    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
