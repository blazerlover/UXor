package ru.exemple.uksorganizer;

import android.app.Application;

import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.db.EventsDatabaseFile;
import ru.exemple.uksorganizer.db.EventsDatabaseSqlite;

public class App extends Application {

    private EventsDatabase eventsDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        eventsDatabase = new EventsDatabaseSqlite(this);
    }

    public EventsDatabase getEventsDb() {
        return eventsDatabase;
    }
}
