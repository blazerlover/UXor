package ru.exemple.uksorganizer.eventNotification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import ru.exemple.uksorganizer.model.Event;
import ru.exemple.uksorganizer.ui.EventActivity;

public class PendingIntentProvider {

    private static final String EXTRA_EVENT = "EVENT";

    private Context context;
    private Event event;

    PendingIntentProvider(Context context, Event event) {
        this.context = context;
        this.event = event;
    }

    public PendingIntent getPendingIntent() {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra(EXTRA_EVENT, event);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

}
