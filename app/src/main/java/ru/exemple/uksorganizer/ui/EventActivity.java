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
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.exemple.uksorganizer.App;
import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.model.Event;


public class EventActivity extends AppCompatActivity {

    private final static String TAG = EventActivity.class.getName();
    private static final String EXTRA_EVENT = "EVENT";

    private EventDetailFragment eventDetailFragment;
    private EventDetailReadOnlyFragment eventDetailReadOnlyFragment;

    private FloatingActionButton imageButton;

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

        eventDetailFragment = new EventDetailFragment();
        eventDetailReadOnlyFragment = new EventDetailReadOnlyFragment();

        imageButton = findViewById(R.id.button_edit_event);

        Event event = (Event) getIntent().getSerializableExtra(EXTRA_EVENT);
        if (event == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, eventDetailFragment).commit();
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp));
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, eventDetailReadOnlyFragment).commit();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        imageButton.setOnClickListener(v -> {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment instanceof EventDetailFragment) {
                ((EventDetailFragment) fragment).saveEvent();
            }
            else if (fragment instanceof EventDetailReadOnlyFragment) {
                editEvent();
            }
        });

        eventsDatabase = ((App) getApplication()).getEventsDb();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof EventDetailFragment) {
            if (eventDetailFragment.eventChanged()) {
                openQuitDialog();
            } else {
                super.onBackPressed();
            }
        } else if (fragment instanceof EventDetailReadOnlyFragment) {
            super.onBackPressed();
        }
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle(R.string.save_changed);
        quitDialog.setPositiveButton(R.string.ok, (dialog, which) -> {
            if (eventDetailFragment.getEvent().getName().length() == 0) {
                openEnterNameDialog();
            } else {
                eventsDatabase.addEvent((eventDetailFragment.getEvent()));
                EventActivity.this.finish();
            }
        });
        quitDialog.setNegativeButton(R.string.cancel, null);
        quitDialog.create();
        quitDialog.show();
    }

    private void openDeleteDialog() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle(R.string.delete_event_question);
        deleteDialog.setPositiveButton(R.string.ok, (dialog, which) -> {
            if (fragment instanceof EventDetailFragment) {
                eventsDatabase.delete(eventDetailFragment.getEvent());
                this.finish();
            } else if (fragment instanceof EventDetailReadOnlyFragment) {
                eventsDatabase.delete(eventDetailReadOnlyFragment.getEvent());
                this.finish();
            }
        });
        deleteDialog.setNegativeButton(R.string.cancel, null);
        deleteDialog.create();
        deleteDialog.show();
    }

    private void openEnterNameDialog() {
        AlertDialog.Builder nameDialog = new AlertDialog.Builder(this);
        nameDialog.setTitle(R.string.enter_name);
        nameDialog.setNegativeButton(R.string.ok, null);
        nameDialog.create();
        nameDialog.show();
    }

    private void editEvent () {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, eventDetailFragment).commit();
        imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp));
    }
}
