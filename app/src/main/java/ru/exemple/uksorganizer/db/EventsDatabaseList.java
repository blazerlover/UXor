package ru.exemple.uksorganizer.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.exemple.uksorganizer.model.Event;

public class EventsDatabaseList implements EventsDatabase {
    Random random = new Random();

    @Override
    public List<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("HUI", categoryCreate(), "hu hui", random.nextLong()));
        events.add(new Event("HUI1", categoryCreate(), "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI2", categoryCreate(), "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI3", categoryCreate(), "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI4", categoryCreate(), "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI5", categoryCreate(), "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI6", categoryCreate(), "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI7", categoryCreate(), "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI8", categoryCreate(), "hu hui", System.currentTimeMillis()));
        events.add(new Event("HUI9", categoryCreate(), "hu hui", System.currentTimeMillis()));
        return events;
    }

    @Override
    public void addEvent(Event event) {

    }

    @Override
    public void update(Event event) {

    }

    public Event.Category categoryCreate() {
        Event.Category randomCategory;
        int categoryNum = random.nextInt(3);
        switch (categoryNum) {
            case 0: randomCategory = Event.Category.ALKO;
            break;
            case 1: randomCategory = Event.Category.MEETING;
            break;
            case 2: randomCategory = Event.Category.SPORT;
            break;
            default: randomCategory = Event.Category.SOMETHING;
        }
        return randomCategory;
    }

}
