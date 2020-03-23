package ru.exemple.uksorganizer.EventNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver extends BroadcastReceiver {

    private Intent intent;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.intent = new Intent(context, NotificationIntentService.class);
        context.startActivity(this.intent);
    }
}
