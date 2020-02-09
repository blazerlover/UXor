package ru.exemple.uksorganizer.db;


import java.io.File;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;


import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
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

    ArrayList<Event> events = new ArrayList<>();
    EventActivity eventActivity;
    Event event;


    @Override
    public List<Event> getAllEvents() {

        try {

            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream( "1.ser"));
            event = (Event) objectInputStream.readObject();
            FileInputStream fis = new FileInputStream("1.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Event event = (Event) ois.readObject();
            ois.close();
            events.add(event);

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
            FileOutputStream fileOutputStream = new FileOutputStream(new File());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("1.ser"));
            objectOutputStream.writeObject(event);
            objectOutputStream.close();
            eventActivity = new EventActivity();
            File directory = eventActivity.getDirectory();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(directory, "1.txt")));
            oos.writeObject(event);
            oos.close();

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Event event) {



    }
}
