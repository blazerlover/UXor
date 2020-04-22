package ru.exemple.uksorganizer.viewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.model.Event;
import ru.exemple.uksorganizer.ui.EventRow;

public class EventRowBinder {

    public EventRow getEventRow(Event event) {
        return new EventRow(event.getName(), bindCategory(event),
                bindTime(event), bindCategoryBackground(event), bindPriority(), bindPriorityColor(event), event);
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

            default:
                return R.color.colorPriorityLow;
        }
    }
}
