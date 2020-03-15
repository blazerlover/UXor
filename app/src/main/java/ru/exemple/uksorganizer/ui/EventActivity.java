package ru.exemple.uksorganizer.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ru.exemple.uksorganizer.App;
import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.model.Event;


public class EventActivity extends AppCompatActivity {

    private static final String EXTRA_EVENT = "EVENT";
    public static final String EXTRA_EVENTDETAIL_ID = "id";
    private final static String TAG = EventActivity.class.getName();

    private EventDetailFragment frag;

    private EventsDatabase eventsDatabase;

    public static void start(Context context, Event event) {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra(EXTRA_EVENT, event);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        eventsDatabase = ((App) getApplication()).getEventsDb();

        frag = (EventDetailFragment) getSupportFragmentManager().findFragmentById(R.id.eventDetailFragment);
        //int eventID = (int) getIntent().getExtras().get(EXTRA_EVENTDETAIL_ID);
        frag.setEventID(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu_eventactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_event:
                openDeleteDialog();
                return true;
            case R.id.settings_item:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle(R.string.save_changed);
        quitDialog.setPositiveButton(R.string.ok, (dialog, which) -> {
            if (frag.getEvent().getName().length() == 0) {
                openEnterNameDialog();
            }
            else {
            eventsDatabase.addEvent((frag.getEvent()));
            EventActivity.this.finish();}
        });
        quitDialog.setNegativeButton(R.string.cancel, null);
        quitDialog.create();
        quitDialog.show();
    }

    private void openDeleteDialog () {
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle(R.string.delete_event_question);
        deleteDialog.setPositiveButton(R.string.ok, (dialog, which) -> {
            eventsDatabase.delete(frag.getEvent());
            this.finish();
        });
        deleteDialog.setNegativeButton(R.string.cancel, null);
        deleteDialog.create();
        deleteDialog.show();
    }

    private void openEnterNameDialog () {
        AlertDialog.Builder nameDialog = new AlertDialog.Builder(this);
        nameDialog.setTitle(R.string.enter_name);
        nameDialog.setNegativeButton(R.string.ok, null);
        nameDialog.create();
        nameDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (frag.eventChanged()) {
            openQuitDialog();
        } else {
            super.onBackPressed();
        }
    }
}
