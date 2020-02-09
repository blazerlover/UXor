package ru.exemple.uksorganizer.db;

import android.content.Context;

import java.util.List;

import ru.exemple.uksorganizer.model.Event;

public interface EventsDatabase {

    List<Event> getAllEvents(Context context);

    void addEvent(Event event, Context context);

    void update(Event event);

}
