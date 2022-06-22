package com.sportmogila.sporttogether.models;

import java.util.ArrayList;
import java.util.Objects;

public class Event {
    private Integer id;
    private String name;
    private String description;
    private String sport;
    private User owner;
    private ArrayList<User> members;
    private Location location;
    private String event_at;

    public Event(Integer id, String name, String description, String sport, User owner, ArrayList<User> members, Location location, String event_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sport = sport;
        this.owner = owner;
        this.members = members;
        this.location = location;
        this.event_at = event_at;
    }

    public Event(String name, String description, String sport, User owner, Location location, String event_at) {
        this.name = name;
        this.description = description;
        this.sport = sport;
        this.owner = owner;
        this.location = location;
        this.event_at = event_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getEventAt() {
        return event_at;
    }

    public void setEventAt(String event_at) {
        this.event_at = event_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(sport, event.sport) && Objects.equals(owner, event.owner) && Objects.equals(members, event.members) && Objects.equals(location, event.location) && Objects.equals(event_at, event.event_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, sport, owner, members, location, event_at);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sport='" + sport + '\'' +
                ", owner=" + owner +
                ", members=" + members +
                ", location=" + location +
                ", event_at=" + event_at +
                '}';
    }
}
