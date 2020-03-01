package ru.exemple.uksorganizer.db;

import android.content.Context;

import java.util.List;

import ru.exemple.uksorganizer.model.Event;

public interface EventsDatabase {

    List<Event> getAllEvents();

    void addEvent(Event event);

    void delete(Event event);

}
