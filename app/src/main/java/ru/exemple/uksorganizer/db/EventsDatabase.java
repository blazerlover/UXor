package ru.exemple.uksorganizer.db;

import java.util.List;

import ru.exemple.uksorganizer.model.Event;

public interface EventsDatabase {

    List<Event> getAllEvents();

    void addEvent(Event event);

    void update(Event event);

}