package ru.exemple.uksorganizer.model;

import java.io.Serializable;

public class Event implements Serializable {

    private final String name;
    private final Category category;
    private final String description;
    private final long time;

    public Event(String name, Category category, String description, long time) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public long getTime() {
        return time;
    }

    public enum Category {
        MEETING, SPORT, ALKO, SOMETHING
    }
}
