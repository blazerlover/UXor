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

    private final static String TAG = EventsDatabaseSqlite.class.getName();
    private final static String DB_NAME_COLUMN = "NAME";
    private final static String DB_CATEGORY_COLUMN = "CATEGORY";
    private final static String DB_DESCRIPTION_COLUMN = "DESCRIPTION";
    private final static String DB_TIME_COLUMN = "TIME";

    public EventsDatabaseSqlite(Context context){
        this.context = context;
    }

    @Override
    public List<Event> getAllEvents() {
        events = new ArrayList<>();
        helper = new EventDataBaseHelper(context);
        database = helper.getWritableDatabase();
        cursor = database.query("EventDataBase", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(DB_NAME_COLUMN));
            Event.Category category;
            String categoryString = cursor.getString(cursor.getColumnIndex(DB_CATEGORY_COLUMN));
            Log.d(TAG, "categoryString = " + categoryString);
            try {
                category = Event.Category.valueOf(categoryString);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.toString());
                category = Event.Category.SOMETHING;
            } catch (NullPointerException e) {
                Log.e(TAG, e.toString());
                category = Event.Category.SOMETHING;
            }
            /*switch (categoryString) {
                case "MEETING": category = Event.Category.MEETING;
                    break;
                case "SPORT" : category = Event.Category.SPORT;
                    break;
                case "ALKO" : category = Event.Category.ALKO;
                    break;
                default: category = Event.Category.SOMETHING;
            }*/
            String description = cursor.getString(cursor.getColumnIndex(DB_DESCRIPTION_COLUMN));
            long time = cursor.getLong(cursor.getColumnIndex(DB_TIME_COLUMN));
            event = new Event(name, category, description, time);
            events.add(event);
        }
        database.close();
        cursor.close();
        return events;
    }

    @Override
    public void addEvent(Event event) {
        contentValues = new ContentValues();
        contentValues.put(DB_NAME_COLUMN, event.getName());
        contentValues.put(DB_CATEGORY_COLUMN, event.getCategory().toString());
        contentValues.put(DB_DESCRIPTION_COLUMN, event.getDescription());
        contentValues.put(DB_TIME_COLUMN, event.getTime());
        database = helper.getWritableDatabase();
        database.insert("EventDataBase", null, contentValues);
        contentValues.clear();
        database.close();
        //временно для очистки DB:
        //helper.getWritableDatabase().delete("EventDataBase", null, null);
    }

    @Override
    public void update(Event event) {
        helper = new EventDataBaseHelper(context);
        database = helper.getWritableDatabase();
        Log.d(TAG, "" + database);
        database.delete("EventDataBase", "NAME = ?", new String[]{event.getName()});
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