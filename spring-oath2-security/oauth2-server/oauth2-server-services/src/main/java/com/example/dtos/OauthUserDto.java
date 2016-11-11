package com.example.dtos;

import java.io.Serializable;

/**
 * @author Aleksandr_Savchenko
 */
public class OauthUserDto implements Serializable {

    private static final long serialVersionUID = -778862342398333712L;

    private String user;
    private String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "OauthUserDto{" +
               "user='" + user + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
