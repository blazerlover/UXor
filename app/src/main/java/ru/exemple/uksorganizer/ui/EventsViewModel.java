package ru.exemple.uksorganizer.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.model.Event;

public class EventsViewModel extends ViewModel {

    private final EventsDatabase eventsDatabase;

    private MutableLiveData<List<EventRow>> liveData = new MutableLiveData<>();

    public EventsViewModel(EventsDatabase eventsDatabase) {
        this.eventsDatabase = eventsDatabase;
    }

    public void load() {
        new Thread() {
            @Override
            public void run() {
                List<Event> events = eventsDatabase.getAllEvents();
                liveData.postValue(getEventRows(events));
            }
        }.start();
    }

    public MutableLiveData<List<EventRow>> getLiveData() {
        return liveData;
    }

    private List<EventRow> getEventRows(List<Event> events) {
        List<EventRow> result = new ArrayList<>();
        for (Event event : events) {
            result.add(getEventRow(event));
        }
        return result;
    }

    private EventRow getEventRow(Event event) {
        //TODO: сделать нормальное преобразование в EventRow
        return new EventRow(event.getName(), event.getCategory().toString(),
                bindTime(event), bindCategoryImage(event), event);
    }

    public void delete(Event event) {
        new Thread() {
            @Override
            public void run() {
                eventsDatabase.delete(event);
                load();
            }
        }.start();
    }

    private int bindCategoryImage(Event event) {
        switch (event.getCategory()) {
            case ALKO:
                return R.drawable.category_alko_shape;
            case MEETING:
                return R.drawable.category_meeting_shape;
            case SPORT:
                return R.drawable.category_sport_shape;
            default:
                return R.drawable.category_something_shape;
        }
    }

    private String bindTime(Event event) {
        Date date = new Date();
        date.setTime(event.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String strTime = String.format("%tb %te %tR", calendar, calendar, calendar);
        return strTime;
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
