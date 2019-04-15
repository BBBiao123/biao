package com.biao.rebot.config;

import lombok.Data;

import java.util.Objects;

/**
 * Login.
 * <p>
 * 登录信息
 * <p>
 * 18-12-26上午11:05
 *
 *  "" sixh
 */
@Data
public class Login {
    private String userId;

    private String userName;

    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Login login = (Login) o;
        return Objects.equals(userId, login.userId) &&
                Objects.equals(userName, login.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName);
    }
}
