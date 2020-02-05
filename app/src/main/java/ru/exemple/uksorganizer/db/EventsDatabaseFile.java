package ru.exemple.uksorganizer.db;

import android.content.Context;
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

    @Override
    public List<Event> getAllEvents() {

        try {
            FileInputStream fis =
                    new FileInputStream("1.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Event event = (Event) ois.readObject();
            ois.close();
            events.add(event);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return events;
    }

    @Override
    public void addEvent(Event event) {

        try {
            File directory = eventActivity.getDirectory();
            ObjectOutputStream oos =
                    new ObjectOutputStream(new FileOutputStream(new File(directory, "1.txt")));
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
