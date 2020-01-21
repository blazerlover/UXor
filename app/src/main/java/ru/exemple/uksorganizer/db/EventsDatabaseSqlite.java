package ru.exemple.uksorganizer.db;

import java.util.List;

import ru.exemple.uksorganizer.model.Event;

//TODO Сделать реализацию через Sqlite
public class EventsDatabaseSqlite implements EventsDatabase {

    @Override
    public List<Event> getAllEvents() {
        return null;
    }

    @Override
    public void addEvent(Event event) {

    }

    @Override
    public void update(Event event) {

    }
}
