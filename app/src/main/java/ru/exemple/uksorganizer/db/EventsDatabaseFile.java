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

    private String [] filelist, trashfilelist;
    public static final String TAG = EventsDatabaseFile.class.getName();

    @Override
    public List<Event> getAllEvents(boolean isDeletedRequestFlag) {

        File directory = new File(context.getFilesDir(), "saving_path");
        File trashDirectory = new File(context.getFilesDir(), "trash_path");

        if(!directory.exists()) {
            directory.mkdirs();
        }
        if(!trashDirectory.exists()) {
            trashDirectory.mkdirs();
        }
        filelist = directory.list();
        trashfilelist = trashDirectory.list();
        ArrayList<Event> events = new ArrayList<>();
        if (!isDeletedRequestFlag) {
            for (String filename : filelist) {
                try {
                    File file = new File(directory, filename);
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    Event event = (Event) ois.readObject();
                    ois.close();
                    events.add(event);
                    } catch (Exception ex) {
                    ex.printStackTrace();
                    }
            }
        }
        if (isDeletedRequestFlag) {
            for (String filename : trashfilelist) {
                try {
                    File file = new File(trashDirectory, filename);
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    Event event = (Event) ois.readObject();
                    ois.close();
                    events.add(event);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
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
    public void delete(Event event) {
        File directory = new File(context.getFilesDir(), "saving_path");
        File trashDirectory = new File(context.getFilesDir(), "trash_path");
        File file = new File(directory, event.getName());
        File trashFile = new File(trashDirectory, event.getName());
        file.renameTo(trashFile);
    }

    @Override
    public void clearTrash() {
        File trashDirectory = new File(context.getFilesDir(), "trash_path");
        trashfilelist = trashDirectory.list();
        if (trashDirectory.isDirectory()) {
            for (String filename : trashfilelist) {
                File file = new File(trashDirectory, filename);
                file.delete();
            }
        }
    }
}