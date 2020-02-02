package ru.exemple.uksorganizer.db;

import android.content.Context;
import android.os.Environment;
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

//TODO Сделать реализацию через Serialisable

public class EventsDatabaseFile implements EventsDatabase{

    EventActivity eventActivity;
    ArrayList<Event> events = new ArrayList<>();

    @Override
    public List<Event> getAllEvents() {

        try {
            Event event = (Event) loadSerializedObject(new File("/Download/save_object.bin"));
            /*File file = new File("/Main storage/Download/1.txt");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Event event = (Event) ois.readObject();*/
            /*FileInputStream fileInputStream = new FileInputStream("1.txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Event event = (Event) objectInputStream.readObject();
            fileInputStream.close();*/
            /*events.add(event);*/
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return events;
    }

    @Override
    public void addEvent(Event event) {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("/Download/save_object.bin")));
            oos.writeObject(event);
            oos.flush();
            oos.close();
            //Classic java serializable
            /*FileOutputStream fileOutputStream = new FileOutputStream("1.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(event);
            fileOutputStream.close();*/
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Event event) {

    }

    public Object loadSerializedObject(File file)
    {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Object o = ois.readObject();
            return o;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
}
