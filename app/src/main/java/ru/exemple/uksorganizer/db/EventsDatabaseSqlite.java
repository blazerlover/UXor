package ru.exemple.uksorganizer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

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
    private Event event;

    private final static String TAG = EventsDatabaseSqlite.class.getName();
    private final static String DB_NAME = "EventDataBase";
    private final static String DB_NAME_COLUMN = "NAME";
    private final static String DB_CATEGORY_COLUMN = "CATEGORY";
    private final static String DB_DESCRIPTION_COLUMN = "DESCRIPTION";
    private final static String DB_TIME_COLUMN = "TIME";
    private final static String DB_PRIORITY_COLUMN = "PRIORITY";

    public EventsDatabaseSqlite(Context context){
        this.context = context;
        helper = new EventDataBaseHelper(context);
        database = helper.getWritableDatabase();
        //database.execSQL("DROP TABLE EventDataBase");
    }

    @Override
    public List<Event> getAllEvents() {
        events = new ArrayList<>();
        try (Cursor cursor = database.query(DB_NAME, null, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                Log.d(TAG, "database = " + database);
                Log.d(TAG, "cursor = " + cursor);
                String name = cursor.getString(cursor.getColumnIndex(DB_NAME_COLUMN));
                Event.Category category;
                String categoryString = cursor.getString(cursor.getColumnIndex(DB_CATEGORY_COLUMN));
                try {
                    category = Event.Category.valueOf(categoryString);
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, e.toString());
                    category = Event.Category.SOMETHING;
                } catch (NullPointerException e) {
                    Log.e(TAG, e.toString());
                    category = Event.Category.SOMETHING;
                }
                String description = cursor.getString(cursor.getColumnIndex(DB_DESCRIPTION_COLUMN));
                Log.d(TAG, "index = " + cursor.getColumnIndex(DB_TIME_COLUMN));
                long time = cursor.getLong(cursor.getColumnIndex(DB_TIME_COLUMN));
                Log.d(TAG, "index = " + cursor.getColumnIndex(DB_PRIORITY_COLUMN));
                int priority = cursor.getInt(cursor.getColumnIndex(DB_PRIORITY_COLUMN));
                event = new Event(name, category, description, time, priority);
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public void addEvent(Event event) {
        contentValues = new ContentValues();
        contentValues.put(DB_NAME_COLUMN, event.getName());
        contentValues.put(DB_CATEGORY_COLUMN, event.getCategory().toString());
        contentValues.put(DB_DESCRIPTION_COLUMN, event.getDescription());
        contentValues.put(DB_TIME_COLUMN, event.getTime());
        contentValues.put(DB_PRIORITY_COLUMN, event.getPriority());
        database.insert(DB_NAME, null, contentValues);
        contentValues.clear();
    }

    @Override
    public void delete(Event event) {
        database.delete(DB_NAME, "NAME = ?", new String[]{event.getName()});
    }

    class EventDataBaseHelper extends SQLiteOpenHelper {

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
                    "TIME INTEGER, " +
                    "PRIORITY INTEGER ) ;");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    //для очистки ДБ при коррективах
    //helper.getWritableDatabase().delete("EventDataBase", null, null);

}