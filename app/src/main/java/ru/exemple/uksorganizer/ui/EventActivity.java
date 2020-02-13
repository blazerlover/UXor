package ru.exemple.uksorganizer.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.exemple.uksorganizer.App;
import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.db.EventsDatabaseFile;
import ru.exemple.uksorganizer.model.Event;

//TODO: добавить реализацию DatePickerDialog перед вызовом TimePickerDialog

public class EventActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription;
    private Spinner spinnerCategory;
    private TextView textViewTime;
    private Button buttonSaveEvent, buttonSetTime, buttonSD;
    private Calendar calendar = Calendar.getInstance();
    private TimePickerDialog timepickerdialog;
    private DatePickerDialog datePickerDialog;
    private int CalendarHour, CalendarMinute;

    private Event event;
    private EventsDatabase eventsDatabase;
    private Event.Category [] categoriesArray = Event.Category.values();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        eventsDatabase = ((App) getApplication()).getEventsDb();
        init();

        buttonSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsDatabase.addEvent(getEvent());
            }
        });
    }

    private void init() {

        editTextName = findViewById(R.id.editTextName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewTime = findViewById(R.id.textViewTime);
        buttonSaveEvent = findViewById(R.id.buttonSaveEvent);
        buttonSetTime = findViewById(R.id.buttonSetTime);
        buttonSD = findViewById(R.id.buttonSD);

        ArrayAdapter<Event.Category> arrayAdapter = new ArrayAdapter<Event.Category>(this, android.R.layout.simple_list_item_1, categoriesArray);
        spinnerCategory.setAdapter(arrayAdapter);

        buttonSD.setOnClickListener(new View.OnClickListener() {
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

        buttonSetTime.setOnClickListener(new View.OnClickListener() {
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
                        }, CalendarHour, CalendarMinute, true);
                timepickerdialog.show();
            }
        });
        setInitialDateTime();
    }

    private void setInitialDateTime() {
        textViewTime.setText(DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }

    public Event getEvent() {
        String name = editTextName.getText().toString();
        Event.Category category = (Event.Category) spinnerCategory.getSelectedItem();
        String description = editTextDescription.getText().toString();
        long time = calendar.getTimeInMillis();
        event = new Event(name, category, description, time);
        return event;
    }
}
