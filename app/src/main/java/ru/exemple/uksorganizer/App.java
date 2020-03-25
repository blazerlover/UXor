package ru.exemple.uksorganizer;

import android.app.Application;

import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.db.EventsDatabaseFile;

public class App extends Application {

    private EventsDatabase eventsDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        eventsDatabase = new EventsDatabaseFile(this);
    }

    public EventsDatabase getEventsDb() {
        return eventsDatabase;
    }
}
