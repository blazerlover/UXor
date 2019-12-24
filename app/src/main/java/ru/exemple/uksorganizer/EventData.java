package ru.exemple.uksorganizer;

public class EventData {

    private String name;
    private String category;
    private String description;

    public void setName (String name1) {
        this.name = name1;
    }

    public String getName() {
        return name;
    }

    public void setCategory (String category1) {
        this.category = category1;
    }

    public String getCategory() {
        return category;
    }

    public void setDescription (String description1) {
        this.description = description1;
    }

    public String getDescription() {
        return description;
    }
}
