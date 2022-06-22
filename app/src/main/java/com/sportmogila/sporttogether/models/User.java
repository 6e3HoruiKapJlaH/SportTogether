package com.sportmogila.sporttogether.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private Integer id;
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

    public Integer getId() {
        return id;
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

    public String getPhotoUrl() {
        return photo_url;
    }

    public void setPhotoUrl(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getGoogleId() {
        return google_id;
    }

    public void setGoogleId(String google_id) {
        this.google_id = google_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && email.equals(user.email) && google_id.equals(user.google_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, google_id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photo_url='" + photo_url + '\'' +
                ", google_id='" + google_id + '\'' +
                '}';
    }
}
