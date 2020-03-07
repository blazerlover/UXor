package ru.exemple.uksorganizer.ui;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;

import ru.exemple.uksorganizer.R;

public class MediaIntentService extends IntentService {

    private MediaPlayer mediaPlayer;
    // TODO: Rename actions, choose action names that describe tasks that this
    public static final String ACTION_FOO = "ru.exemple.uksorganizer.ui.action.FOO";
    public static final String ACTION_BAZ = "ru.exemple.uksorganizer.ui.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "ru.exemple.uksorganizer.ui.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "ru.exemple.uksorganizer.ui.extra.PARAM2";

    public MediaIntentService() {
        super("MediaIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mediaPlayer = MediaPlayer.create(this, R.raw.pass);
        mediaPlayer.start();

    }
}
