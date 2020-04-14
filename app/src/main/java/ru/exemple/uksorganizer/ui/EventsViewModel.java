package ru.exemple.uksorganizer.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.model.Event;

public class EventsViewModel extends ViewModel {

    public final static String TAG = EventsViewModel.class.getName();

    private final EventsDatabase eventsDatabase;

    private MutableLiveData<List<EventRow>> liveData = new MutableLiveData<>();
    private List<EventRow> eventRows;

    public EventsViewModel(EventsDatabase eventsDatabase) {
        this.eventsDatabase = eventsDatabase;
    }

    public void load(boolean isDeletedRequestFlag) {
        new Thread() {
            @Override
            public void run() {
                try{TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {
                    e.printStackTrace();
                }
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


    private List<EventRow> getEventRows(List<Event> events) {
        List<EventRow> result = new ArrayList<>();
        for (Event event : events) {
            result.add(getEventRow(event));
        }
        return result;
    }

    private EventRow getEventRow(Event event) {
        return new EventRow(event.getName(), bindCategory(event),
                bindTime(event), bindCategoryBackground(event), bindPriority(), bindPriorityColor(event), event);
    }

    public void delete(Event event, boolean isDeletedRequestFlag) {
        new Thread() {
            @Override
            public void run() {
                eventsDatabase.delete(event);
                load(isDeletedRequestFlag);
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

    private int bindCategory(Event event) {
        switch (event.getCategory()) {
            case WORK:
                return R.drawable.ic_work_outline_black_24dp;
            case MEETING:
                return R.drawable.ic_people_black_24dp;
            case SPORT:
                return R.drawable.ic_sports_basketball_black_24dp;
            default:
                return R.drawable.ic_description_24px;
        }
    }

    private String bindTime(Event event) {
        SimpleDateFormat df = new SimpleDateFormat("MMM dd hh:mm", Locale.getDefault());
        return df.format(event.getTime());
    }

    private int bindCategoryBackground(Event event) {
        switch (event.getCategory()) {
            case WORK:
                return R.drawable.category_work_shape;
            case MEETING:
                return R.drawable.category_meeting_shape;
            case SPORT:
                return R.drawable.category_sport_shape;
            default:
                return R.drawable.category_something_shape;
        }
    }

    private int bindPriority() {
        return R.drawable.ic_priority_high_white_24dp;
    }

    private int bindPriorityColor(Event event) {
        switch (event.getPriority()) {
            case 0:
                return R.color.colorPriorityLow;
            case 1:
                return R.color.colorPriorityMiddle;
            case 2:
                return R.color.colorPriorityHard;

            default: return R.color.colorPriorityLow;
        }

       /* if (event.getPriority() == 1) {
        return R.color.colorPriorityHard;
        }
        else return R.color.colorPriorityLow;*/
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
