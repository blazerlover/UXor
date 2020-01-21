package ru.exemple.uksorganizer;

import android.app.Application;

import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.db.EventsDatabaseList;

public class App extends Application {

    private EventsDatabase eventsDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        eventsDatabase = new EventsDatabaseList();
    }

    public EventsDatabase getEventsDb() {
        return eventsDatabase;
    }

}
