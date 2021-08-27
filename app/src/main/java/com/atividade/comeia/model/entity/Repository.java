package com.atividade.comeia.model.entity;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Repository {

    private Long id;
    private String name;
    private Owner owner;
    @SerializedName("html_url")
    private String htmlUrl;
    private String description;
    private String language;
    @SerializedName("stargazers_count")
    private Integer stars;

    public Repository(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    @NonNull
    @Override
    public String toString() {
        return name+"\n";
    }
}
