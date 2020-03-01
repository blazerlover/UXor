package ru.exemple.uksorganizer.ui;

import ru.exemple.uksorganizer.model.Event;

public class EventRow {

    public final String title;
    public final String category;
    public final String time;
    public final int image;
    public final Event event;

    public EventRow(String title, String category, String time, int image, Event event) {
        this.title = title;
        this.category = category;
        this.time = time;
        this.image = image;
        this.event = event;
    }

}
