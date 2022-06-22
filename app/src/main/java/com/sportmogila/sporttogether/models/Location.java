package com.sportmogila.sporttogether.models;

import java.io.Serializable;
import java.util.Objects;

public class Location implements Serializable {
    private String country;
    private String city;
    private Double map_x;
    private Double map_y;

    public Location(String country, String city, Double map_x, Double map_y) {
        this.country = country;
        this.city = city;
        this.map_x = map_x;
        this.map_y = map_y;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getMapX() {
        return map_x;
    }

    public void setMapX(Double map_x) {
        this.map_x = map_x;
    }

    public Double getMapY() {
        return map_y;
    }

    public void setMapY(Double map_y) {
        this.map_y = map_y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(country, location.country) && Objects.equals(city, location.city) && Objects.equals(map_x, location.map_x) && Objects.equals(map_y, location.map_y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, map_x, map_y);
    }

    @Override
    public String toString() {
        return "Location{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", map_x=" + map_x +
                ", map_y=" + map_y +
                '}';
    }
}
