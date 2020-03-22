package ru.exemple.uksorganizer.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.exemple.uksorganizer.model.Event;

public class EventsDatabaseList implements EventsDatabase {

    Random random = new Random();

    @Override
    public List<Event> getAllEvents(boolean isDeletedRequestFlag) {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("0", categoryCreate(), "go 0", random.nextLong(), 1));
        events.add(new Event("1", categoryCreate(), "go 1", System.currentTimeMillis(),0));
        events.add(new Event("2", categoryCreate(), "go 2", System.currentTimeMillis(),1));
        events.add(new Event("3", categoryCreate(), "go 3", System.currentTimeMillis(),0));
        events.add(new Event("4", categoryCreate(), "go 4", System.currentTimeMillis(),1));
        events.add(new Event("5", categoryCreate(), "go 5", System.currentTimeMillis(),0));
        events.add(new Event("6", categoryCreate(), "go 6", System.currentTimeMillis(),1));
        events.add(new Event("7", categoryCreate(), "go 7", System.currentTimeMillis(),0));
        events.add(new Event("8", categoryCreate(), "go 8", System.currentTimeMillis(),1));
        events.add(new Event("9", categoryCreate(), "go 9", System.currentTimeMillis(),0));
        return events;
    }

    @Override
    public void addEvent(Event event) {

    }

    @Override
    public void delete (Event event) {

    }

    @Override
    public void clearTrash() {

    }

    public Event.Category categoryCreate() {
        Event.Category randomCategory;
        int categoryNum = random.nextInt(3);
        switch (categoryNum) {
            case 0: randomCategory = Event.Category.WORK;
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
