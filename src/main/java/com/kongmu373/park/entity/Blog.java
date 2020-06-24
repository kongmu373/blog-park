package com.kongmu373.park.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName(value = "blogs")
public class Blog {
    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private String content;
    private Instant updatedAt;
    private Instant createdAt;

    /**
     * 是否删除
     */
    @TableLogic
    @JsonIgnore
    private Boolean deleted;


    public Integer getId() {
        return id;
    }

    public Blog setId(Integer id) {
        this.id = id;
        return this;
    }


    public String getTitle() {
        return title;
    }

    public Blog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Blog setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Blog setContent(String content) {
        this.content = content;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Blog setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Blog setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public Blog setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Blog setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog blog = (Blog) o;
        return Objects.equals(id, blog.id) &&
                Objects.equals(userId, blog.userId) &&
                Objects.equals(title, blog.title) &&
                Objects.equals(description, blog.description) &&
                Objects.equals(content, blog.content) &&
                Objects.equals(updatedAt, blog.updatedAt) &&
                Objects.equals(createdAt, blog.createdAt) &&
                Objects.equals(deleted, blog.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, title, description, content, updatedAt, createdAt, deleted);
    }
}

