package com.boscotec.andelachallenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Johnbosco on 15-Jul-17.
 */

public class UserDetail {

    @SerializedName("login")
    @Expose
    private String login;

    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("html_url")
    @Expose
    private String htmlUrl;

    public UserDetail(String login, String avatarUrl, String url, String htmlUrl) {
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.url = url;
        this.htmlUrl = htmlUrl;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

}
