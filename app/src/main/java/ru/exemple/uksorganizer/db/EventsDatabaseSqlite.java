package ru.exemple.uksorganizer.db;

import android.content.Context;

import java.util.List;

import ru.exemple.uksorganizer.model.Event;

//TODO Сделать реализацию через Sqlite
public class EventsDatabaseSqlite implements EventsDatabase {

    @Override
    public List<Event> getAllEvents(Context context) {
        return null;
    }

    @Override
    public void addEvent(Event event, Context context) {

    }

    @Override
    public void update(Event event) {

    }
}
