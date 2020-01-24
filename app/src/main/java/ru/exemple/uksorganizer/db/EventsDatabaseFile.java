package ru.exemple.uksorganizer.db;

import java.io.Serializable;
import java.util.List;

import ru.exemple.uksorganizer.model.Event;
import ru.exemple.uksorganizer.ui.EventActivity;

//TODO Сделать реализацию через Serialisable

// HUI
public class EventsDatabaseFile implements EventsDatabase{

    EventActivity eventActivity;

    @Override
    public List<Event> getAllEvents() {
        return null;
    }

    @Override
    public void addEvent(Event event) {

        eventActivity.getEvent();



    }

    @Override
    public void update(Event event) {

    }
}
