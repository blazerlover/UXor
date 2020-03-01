package ru.exemple.uksorganizer.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.exemple.uksorganizer.App;
import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.model.Event;


public class EventActivity extends AppCompatActivity implements SimpleDialogFragment.SimpleDialogListener {

    private static final String EXTRA_EVENT = "EVENT";

    private EditText editTextName, editTextDescription;
    private Spinner spinnerCategory;
    private TextView textViewTime;
    private TextView textViewDate;
    private Button buttonSaveEvent;
    private Calendar calendar = Calendar.getInstance();
    private TimePickerDialog timepickerdialog;
    private DatePickerDialog datePickerDialog;

    private Event event;
    private EventsDatabase eventsDatabase;
    private Event.Category [] categoriesArray = Event.Category.values();
    private final static String TAG = EventActivity.class.getName();

    public static void start(Context context, Event event) {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra(EXTRA_EVENT, event);
        context.startActivity(intent);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, EventActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        eventsDatabase = ((App) getApplication()).getEventsDb();
        init();
    }

    private void init() {
        editTextName = findViewById(R.id.editTextName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewTime = findViewById(R.id.textViewTime);
        textViewDate = findViewById(R.id.textViewDate);
        buttonSaveEvent = findViewById(R.id.buttonSaveEvent);

        ArrayAdapter<Event.Category> arrayAdapter = new ArrayAdapter<Event.Category>(this, android.R.layout.simple_list_item_1, categoriesArray);
        spinnerCategory.setAdapter(arrayAdapter);

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(EventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        setInitialDateTime();
                    }
                }, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timepickerdialog = new TimePickerDialog(EventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                setInitialDateTime();
                            }
                        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
                timepickerdialog.show();
            }
        });

        buttonSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EventActivity.this.getEvent().getName().length() == 0) {
                    openEnterNameDialog();
                } else {
                    eventsDatabase.addEvent((EventActivity.this.getEvent()));
                    EventActivity.this.finish();
                }
            }
        });
        setInitialDateTime();
        getIntentFromMain();
    }

    private void setInitialDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        SimpleDateFormat stf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        textViewDate.setText(sdf.format(calendar.getTimeInMillis()));
        textViewTime.setText(stf.format(calendar.getTimeInMillis()));
    }

    private void getIntentFromMain() {
        event = (Event) getIntent().getSerializableExtra(EXTRA_EVENT);
        if (event == null) {
            event = new Event("", Event.Category.SOMETHING, "", System.currentTimeMillis());
        }
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getTime());

        editTextName.setText(event.getName());
        editTextDescription.setText(event.getDescription());
        SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        SimpleDateFormat tf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        textViewDate.setText(df.format(event.getTime()));
        textViewTime.setText(tf.format(event.getTime()));
    }

    public Event getEvent() {
        String name = editTextName.getText().toString();
        Event.Category category = (Event.Category) spinnerCategory.getSelectedItem();
        String description = editTextDescription.getText().toString();
        long time = calendar.getTimeInMillis();
        return new Event(name, category, description, time);
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
                DialogFragment dialog = new SimpleDialogFragment();
                dialog.show(getSupportFragmentManager(), "SimpleDialogFragment");
                return true;
            case R.id.settings_item:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onDeleteDialogPositiveClick(DialogFragment dialog) {
        eventsDatabase.update(this.getEvent());
        this.finish();
    }

    @Override
    public void onDeleteDialogNegativeClick(DialogFragment dialog) {

    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle(R.string.save_changed);
        quitDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eventsDatabase.addEvent((EventActivity.this.getEvent()));
                EventActivity.this.finish();
            }
        });
        quitDialog.setNegativeButton(R.string.cancel, null);
        quitDialog.create();
        quitDialog.show();
    }

    private void openEnterNameDialog () {
        final AlertDialog.Builder namedialog = new AlertDialog.Builder(this);
        namedialog.setTitle("Enter name");
        namedialog.setNegativeButton(R.string.ok, null);
        namedialog.create();
        namedialog.show();
    }

    @Override
    public void onBackPressed() {
        if (eventChanged()) {
            openQuitDialog();
        } else {
            super.onBackPressed();
        }
    }

    private boolean eventChanged() {
        Event newEvent = getEvent();
        return !event.equals(newEvent);
    }

}
