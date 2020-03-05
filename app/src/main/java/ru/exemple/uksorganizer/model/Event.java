package ru.exemple.uksorganizer.model;

import java.io.Serializable;

public class Event implements Serializable {

    private final String name;
    private final Category category;
    private final String description;
    private final long time;
    private final int priority;

    public Event(String name, Category category, String description, long time, int priority) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.time = time;
        this.priority = priority;
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

    public int getPriority(){
        return priority;
    }

    public enum Category {
        MEETING, SPORT, ALKO, SOMETHING
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (time != event.time) return false;
        if (name != null ? !name.equals(event.name) : event.name != null) return false;
        if (category != event.category) return false;
        return description != null ? description.equals(event.description) : event.description == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }
}
