package ru.exemple.uksorganizer.db;

import java.util.ArrayList;
import java.util.List;

import ru.exemple.uksorganizer.model.Event;

public class EventsDatabaseList implements EventsDatabase {

    @Override
    public List<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("HUI0", Event.Category.ALKO, "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI1", Event.Category.SPORT, "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI2", Event.Category.ALKO, "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI3", Event.Category.ALKO, "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI4", Event.Category.ALKO, "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI5", Event.Category.ALKO, "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI6", Event.Category.ALKO, "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI7", Event.Category.ALKO, "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI8", Event.Category.ALKO, "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI9", Event.Category.ALKO, "hu hui", System.currentTimeMillis()));
        return events;
    }

    @Override
    public void addEvent(Event event) {

    }

    @Override
    public void update(Event event) {

    }

}
