package com.kongmu373.park.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName(value = "blogs")
public class BlogVo implements Serializable {
    private Integer id;
    private User user;
    private String title;
    private String description;
    private String content;
    private Instant updatedAt;
    private Instant createdAt;


    public BlogVo() {

    }

    public Integer getId() {
        return id;
    }

    public BlogVo setId(Integer id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public BlogVo setUser(User user) {
        this.user = user;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BlogVo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public BlogVo setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getContent() {
        return content;
    }

    public BlogVo setContent(String content) {
        this.content = content;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public BlogVo setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public BlogVo setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }



}
