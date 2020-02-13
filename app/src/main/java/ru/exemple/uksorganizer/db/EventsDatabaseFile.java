package ru.exemple.uksorganizer.db;

import android.content.Context;
import android.util.Log;

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
    private ArrayList<Event> events = new ArrayList<>();
    private File directory;
    private File file;
    private String [] filelist;
    private String TAG = "LOGS";

    @Override
    public List<Event> getAllEvents() {

        directory = context.getFilesDir();
        filelist = directory.list();
        Log.d(TAG, filelist[0]);

        try {
            for (String filename: filelist) {
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

        try {
            directory = context.getFilesDir();
            file = new File(directory, event.getName());
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