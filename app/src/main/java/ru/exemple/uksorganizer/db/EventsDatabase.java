package ru.exemple.uksorganizer.db;

import java.util.List;

import ru.exemple.uksorganizer.model.Event;

public interface EventsDatabase {

    List<Event> getAllEvents(boolean isDeletedRequestFlag);

    void addEvent(Event event);

    void delete(Event event);

    void clearTrash();

    void setOnDataChangedListener(OnDataChangedListener listener);

    interface OnDataChangedListener {
        void onDataChanged();
    }
}
