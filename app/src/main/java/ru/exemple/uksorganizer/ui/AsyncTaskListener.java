package ru.exemple.uksorganizer.ui;

import java.util.ArrayList;

import ru.exemple.uksorganizer.model.Event;

public interface AsyncTaskListener {
        void onAsyncTaskFinished(ArrayList<Event> events);
    }
