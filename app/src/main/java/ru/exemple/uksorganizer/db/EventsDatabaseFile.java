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
    MainActivity mainActivity;
    File directory;

    @Override
    public List<Event> getAllEvents(Context context) {

        mainActivity = (MainActivity) context;

        try {
            directory = mainActivity.getFilesDir();
            File file = new File(directory, "1");
            FileInputStream fis = new FileInputStream(file);
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
    public void addEvent(Event event, Context context) {

        try {
            eventActivity = (EventActivity) context;

            directory = eventActivity.getFilesDir();

            File file = new File(directory, "1");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
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