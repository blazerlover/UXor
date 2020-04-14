package ru.exemple.uksorganizer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ru.exemple.uksorganizer.model.Event;


public class EventsDatabaseSqlite implements EventsDatabase {

    private final static String TAG = EventsDatabaseSqlite.class.getName();
    private final static String DB_NAME = "EventDataBase";
    private final static String DB_EVENTS_TABLE = "EVENTS";
    private final static String DB_NAME_COLUMN = "NAME";
    private final static String DB_CATEGORY_COLUMN = "CATEGORY";
    private final static String DB_DESCRIPTION_COLUMN = "DESCRIPTION";
    private final static String DB_TIME_COLUMN = "TIME";
    private final static String DB_PRIORITY_COLUMN = "PRIORITY";
    private final static String DB_DELETED_COLUMN = "DELETED";

    private EventDataBaseHelper helper;
    private ContentValues contentValues;
    private SQLiteDatabase database;
    private ArrayList<Event> events;
    private Event event;

    public EventsDatabaseSqlite(Context context){
        helper = new EventDataBaseHelper(context);
        database = helper.getWritableDatabase();
    }

    @Override
    public List<Event> getAllEvents(boolean isDeletedRequestFlag) {
        int isDeletedRequest = 1;
        if (!isDeletedRequestFlag) {
            isDeletedRequest = 0;
        }
        events = new ArrayList<>();
            try (Cursor cursor = database.query(DB_EVENTS_TABLE, null, "DELETED = ?",
                    new String[] {Integer.toString(isDeletedRequest)}, null, null, null)) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(DB_NAME_COLUMN));
                    Event.Category category;
                    String categoryString = cursor.getString(cursor.getColumnIndex(DB_CATEGORY_COLUMN));
                    try {
                        category = Event.Category.valueOf(categoryString);
                    } catch (IllegalArgumentException e) {
                        category = Event.Category.CATEGORY;
                    } catch (NullPointerException e) {
                        category = Event.Category.CATEGORY;
                    }
                    String description = cursor.getString(cursor.getColumnIndex(DB_DESCRIPTION_COLUMN));
                    long time = cursor.getLong(cursor.getColumnIndex(DB_TIME_COLUMN));
                    int priority = cursor.getInt(cursor.getColumnIndex(DB_PRIORITY_COLUMN));
                    event = new Event(name, category, description, time, priority);
                    events.add(event);
                }
            }
        return events;
    }

    @Override
    public void addEvent(Event event) {
        contentValues = fillContentValuesByEvent(event, 0);
        database.insert(DB_EVENTS_TABLE, null, contentValues);
        contentValues.clear();
    }

    @Override
    public void delete(Event event) {
        contentValues = fillContentValuesByEvent(event, 1);
        database.update(DB_EVENTS_TABLE, contentValues, "NAME = ?",
                new String[]{event.getName()});
    }

    @Override
    public void clearTrash() {
        database.delete(DB_EVENTS_TABLE,  "DELETED = ?", new String[]{"1"});
    }

    class EventDataBaseHelper extends SQLiteOpenHelper {

        private final static int DB_VERSION = 2;

        public EventDataBaseHelper(Context context) {
         super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE EVENTS ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME TEXT, " +
                    "CATEGORY TEXT, " +
                    "DESCRIPTION TEXT, " +
                    "TIME INTEGER, " +
                    "PRIORITY INTEGER, " +
                    "DELETED INTEGER) ;");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion == 1) {
                db.execSQL("ALTER TABLE EVENTSDATABASE RENAME TO EVENTS");
                db.execSQL("ALTER TABLE EVENTS ADD COLUMN DELETED INTEGER;");
            }
        }
    }

    private ContentValues fillContentValuesByEvent(Event event, int isDeleted) {
        contentValues = new ContentValues();
        contentValues.put(DB_NAME_COLUMN, event.getName());
        contentValues.put(DB_CATEGORY_COLUMN, event.getCategory().toString());
        contentValues.put(DB_DESCRIPTION_COLUMN, event.getDescription());
        contentValues.put(DB_TIME_COLUMN, event.getTime());
        contentValues.put(DB_PRIORITY_COLUMN, event.getPriority());
        contentValues.put(DB_DELETED_COLUMN, isDeleted);
        return contentValues;
    }
}