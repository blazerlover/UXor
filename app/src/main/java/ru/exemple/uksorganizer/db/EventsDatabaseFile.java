package ru.exemple.uksorganizer.db;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import ru.exemple.uksorganizer.model.Event;
import ru.exemple.uksorganizer.ui.EventActivity;


public class EventsDatabaseFile implements EventsDatabase{

    public EventsDatabaseFile(Context context) {
        this.context = context;
    }
    private Context context;
    EventActivity eventActivity;
    ArrayList<Event> events = new ArrayList<>();
    File directory;
    File file;

    @Override
    public List<Event> getAllEvents() {

        try {
            directory = context.getFilesDir();
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
    public void addEvent(Event event) {

        try {
            directory = context.getFilesDir();
            file = new File(directory, "1");
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