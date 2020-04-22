package ru.exemple.uksorganizer.viewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.model.Event;
import ru.exemple.uksorganizer.ui.EventRow;

public class EventsViewModel extends ViewModel implements EventsDatabase.OnDataChangedListener {

    public final static String TAG = EventsViewModel.class.getName();

    private final EventsDatabase eventsDatabase;

    private MutableLiveData<List<EventRow>> liveData = new MutableLiveData<>();
    private EventRowBinder eventRowBinder = new EventRowBinder();
    private List<EventRow> eventRows;
    private boolean isDeletedRequestFlag;

    private EventsViewModel(EventsDatabase eventsDatabase) {
        this.eventsDatabase = eventsDatabase;
    }

    public void load(boolean isDeletedRequestFlag) {
        eventsDatabase.setOnDataChangedListener(this);
        this.isDeletedRequestFlag = isDeletedRequestFlag;
        new Thread() {
            @Override
            public void run() {
                List<Event> events = eventsDatabase.getAllEvents(isDeletedRequestFlag);
                eventRows = getEventRows(events);
                liveData.postValue(eventRows);
            }
        }.start();
    }

    public void sortEventRowsBy(int sortTag) {
        switch (sortTag) {
            case 0:
                Collections.sort(eventRows, ((o1, o2) -> (int) (o1.event.getTime() - o2.event.getTime())));
                break;
            case 1:
                Collections.sort(eventRows, (o1, o2) -> (o2.event.getPriority() - o1.event.getPriority()));
                break;
            case 2:
                Collections.sort(eventRows, (o1, o2) -> o1.title.compareTo(o2.title));
                break;
        }
    }

    public List<EventRow> getSortedEventRows() {
        return eventRows;
    }

    public MutableLiveData<List<EventRow>> getLiveData() {
        return liveData;
    }


    //    Эти методы для делигации управления ViewModel с данными вместо прямых обращений с БД
    public void delete(Event event) {
        eventsDatabase.setOnDataChangedListener(this);
        new Thread() {
            @Override
            public void run() {
                eventsDatabase.delete(event);
//                load(isDeletedRequestFlag);
            }
        }.start();
    }

    //    Эти методы для делигации управления ViewModel с данными вместо прямых обращений с БД
    public void addEvent(Event event) {
        eventsDatabase.setOnDataChangedListener(this);
        new Thread() {
            @Override
            public void run() {
                eventsDatabase.addEvent(event);
//                load(isDeletedRequestFlag);
            }
        }.start();
    }

    public void clearTrash(boolean isDeletedRequestFlag) {
        new Thread() {
            @Override
            public void run() {
                eventsDatabase.clearTrash();
                load(isDeletedRequestFlag);
            }
        }.start();
    }

    private List<EventRow> getEventRows(List<Event> events) {
        List<EventRow> result = new ArrayList<>();
        for (Event event : events) {
            result.add(eventRowBinder.getEventRow(event));
        }
        return result;
    }

    @Override
    public void onDataChanged() {
        load(isDeletedRequestFlag);
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final EventsDatabase eventsDatabase;

        public Factory(EventsDatabase eventsDatabase) {
            this.eventsDatabase = eventsDatabase;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new EventsViewModel(eventsDatabase);
        }
    }

}
