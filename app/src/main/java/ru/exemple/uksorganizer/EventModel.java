package ru.exemple.uksorganizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventModel {

    private DBHelper dbHelper;

    EventData eventData = new EventData();

    public EventModel(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "mydatabase", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "category text,"
                    + "description text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void saveEvent () {

        ContentValues contentValues = new ContentValues();
        String name = eventData.getName();
        String category = eventData.getCategory();
        String description = eventData.getDescription();

        contentValues.put("name", name);
        contentValues.put("category", category);
        contentValues.put("description", description);

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.insert("mytable", null, contentValues);

    }

   /* public void readEvent () {

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query("mytable", null, null, null, null, null, null);

    }*/
}
