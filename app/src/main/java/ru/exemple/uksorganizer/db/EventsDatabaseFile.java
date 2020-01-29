package ru.exemple.uksorganizer.db;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.exemple.uksorganizer.model.Event;
import ru.exemple.uksorganizer.ui.EventActivity;
import ru.exemple.uksorganizer.ui.MainActivity;

//TODO Сделать реализацию через Serialisable

public class EventsDatabaseFile implements EventsDatabase{

    EventActivity eventActivity;
    Event event;

    @Override
    public List<Event> getAllEvents() {

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream( "1.ser"));
            event = (Event) objectInputStream.readObject();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        ArrayList<Event> events = new ArrayList<>();
        /*events.add(new Event(event.getName(), event.getCategory(), event.getDescription(),event.getTime()));*/
        events.add(new Event("HUI0", Event.Category.ALKO, "hu hui", System.currentTimeMillis()));
        return events;
    }

    @Override
    public void addEvent(Event event) {

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("1.ser"));
            objectOutputStream.writeObject(event);
            objectOutputStream.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Event event) {



    }
}
