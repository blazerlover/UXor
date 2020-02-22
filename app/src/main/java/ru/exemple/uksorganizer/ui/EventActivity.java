package ru.exemple.uksorganizer.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
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

    private EditText editTextName, editTextDescription;
    private Spinner spinnerCategory;
    private TextView textViewTime;
    private TextView textViewDate;
    private Button buttonSaveEvent;
    private Calendar calendar = Calendar.getInstance();
    private TimePickerDialog timepickerdialog;
    private DatePickerDialog datePickerDialog;
    final int DIALOG_EXIT = 1;
    final int DIALOG_DELETE_EVENT = 2;

    private Event event;
    private EventsDatabase eventsDatabase;
    private Event.Category [] categoriesArray = Event.Category.values();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        eventsDatabase = ((App) getApplication()).getEventsDb();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        eventsDatabase = ((App) getApplication()).getEventsDb();
        init();

        buttonSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsDatabase.addEvent((EventActivity.this.getEvent()));
            }
        });

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
        setInitialDateTime();
    }

    private void setInitialDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        SimpleDateFormat stf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        textViewDate.setText(sdf.format(calendar.getTimeInMillis()));
        textViewTime.setText(stf.format(calendar.getTimeInMillis()));
    }

    public Event getEvent() {
        String name = editTextName.getText().toString();
        Event.Category category = (Event.Category) spinnerCategory.getSelectedItem();
        String description = editTextDescription.getText().toString();
        long time = calendar.getTimeInMillis();
        return event = new Event(name, category, description, time);
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
    public void onDialogPositiveClick(DialogFragment dialog) {
        eventsDatabase.update(this.getEvent());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle(R.string.save_changed);
        quitDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eventsDatabase.addEvent((EventActivity.this.getEvent()));
            }
        });
        quitDialog.setNegativeButton(R.string.cancel, null);
        quitDialog.create();
        quitDialog.show();
    }

    @Override
    public void onBackPressed() {
        openQuitDialog();
    }
}
