package com.sportmogila.sporttogether.models;

import android.net.Uri;

public class User {
    private String name;
    private String email;
    private String photo_url;
    private String google_id = null;

    public User(String name, String email, String photo_url, String google_id) {
        this.name = name;
        this.email = email;
        this.photo_url = photo_url;
        this.google_id = google_id;
    }

    public User(String name, String email, String photo_url) {
        this.name = name;
        this.email = email;
        this.photo_url = photo_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photo_url=" + photo_url +
                ", google_id='" + google_id + '\'' +
                '}';
    }
}
