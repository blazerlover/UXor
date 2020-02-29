package ru.exemple.uksorganizer;

import android.app.Application;
import android.content.Context;

import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.db.EventsDatabaseFile;
import ru.exemple.uksorganizer.db.EventsDatabaseList;
import ru.exemple.uksorganizer.db.EventsDatabaseSqlite;

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
