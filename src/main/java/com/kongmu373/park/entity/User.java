package com.kongmu373.park.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName(value = "users")
public class User implements Serializable {
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String avatar;
    private Instant createdAt;
    private Instant updatedAt;
    /**
     * 是否删除
     */
    @TableLogic
    @JsonIgnore
    private Boolean deleted;

    public User() {

    }

    public User(Integer id, String username, String password, String avatar, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public User setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public User setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public User setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public User setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(avatar, user.avatar) &&
                Objects.equals(createdAt, user.createdAt) &&
                Objects.equals(updatedAt, user.updatedAt) &&
                Objects.equals(deleted, user.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, avatar, createdAt, updatedAt, deleted);
    }
}
