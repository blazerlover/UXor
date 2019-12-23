package ru.exemple.uksorganizer;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class EventPresenter {

    private EventModel eventModel;
    private EventActivity eventActivity;

    public EventPresenter(EventModel eventModel, EventActivity eventActivity) {
        this.eventModel = eventModel;
        this.eventActivity = eventActivity;
    }

    public void save() {
        eventActivity.getEventData();
            eventModel.saveEvent();
    }
}
