package com.share.jack.greendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 *
 */
@Entity
public class UserInfo {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    @Property(nameInDb = "USER_NAME")
    private String username;

    @Property(nameInDb = "TOKEN")
    private String token;

    @Generated(hash = 881060104)
    public UserInfo(Long id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
