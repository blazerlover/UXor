package ru.exemple.uksorganizer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.exemple.uksorganizer.model.Event;

//TODO Сделать реализацию через Sqlite
public class EventsDatabaseSqlite implements EventsDatabase {

    private Context context;
    private EventDataBaseHelper helper;
    private ContentValues contentValues;
    private SQLiteDatabase database;
    private ArrayList<Event> events;
    private Cursor cursor;
    private Event event;

    private final static String TAG = "MY LOG:";

    public EventsDatabaseSqlite(Context context){
        this.context = context;
    }

    @Override
    public List<Event> getAllEvents() {
        events = new ArrayList<>();
        helper = new EventDataBaseHelper(context);
        database = helper.getWritableDatabase();
        cursor = database.query("EventDataBase", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("NAME"));
                Event.Category category;
                String categoryString = cursor.getString(cursor.getColumnIndex("CATEGORY"));
                switch (categoryString) {
                    case "MEETING": category = Event.Category.MEETING;
                    break;
                    case "SPORT" : category = Event.Category.SPORT;
                    break;
                    case "ALKO" : category = Event.Category.ALKO;
                    break;
                    default: category = Event.Category.SOMETHING;
                }
                String description = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
                long time = cursor.getLong(cursor.getColumnIndex("TIME"));
                event = new Event(name, category, description, time);
                events.add(event);
            } while (cursor.moveToNext());
        }
        database.close();
        return events;
    }

    @Override
    public void addEvent(Event event) {
        contentValues = new ContentValues();
        contentValues.put("NAME", event.getName());
        contentValues.put("CATEGORY", event.getCategory().toString());
        contentValues.put("DESCRIPTION", event.getDescription());
        contentValues.put("TIME", event.getTime());
        database = helper.getWritableDatabase();
        database.insert("EventDataBase", null, contentValues);
        contentValues.clear();
        //временно для очистки DB:
        //helper.getWritableDatabase().delete("EventDataBase", null, null);
    }

    @Override
    public void update(Event event) {

    }

    class EventDataBaseHelper extends SQLiteOpenHelper {

        private final static String DB_NAME = "EventDataBase";
        private final static int DB_VERSION = 1;

        public EventDataBaseHelper(Context context) {
         super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE EventDataBase ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT, " +
                    "CATEGORY TEXT, " +
                    "DESCRIPTION TEXT, " +
                    "TIME INTEGER) ;");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}