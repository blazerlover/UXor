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


public class EventsDatabaseFile implements EventsDatabase{

    public EventsDatabaseFile(Context context) {
        this.context = context;
    }
    private Context context;

    String [] filelist;
    public static final String TAG = EventsDatabaseFile.class.getName();

    @Override
    public List<Event> getAllEvents() {
        File directory = new File(context.getFilesDir(), "saving_path");
        if(!directory.exists()) {
            directory.mkdirs();
        }
        filelist = directory.list();
        ArrayList<Event> events = new ArrayList<>();

        try {
                for (String filename : filelist) {
                    File file = new File(directory, filename);
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    Event event = (Event) ois.readObject();
                    ois.close();
                    events.add(event);
                }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return events;
    }

    @Override
    public void addEvent(Event event) {
        File directory = new File(context.getFilesDir(), "saving_path");
        try {
            File file = new File(directory, event.getName());
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