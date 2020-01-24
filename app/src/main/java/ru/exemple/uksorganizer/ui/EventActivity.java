package ru.exemple.uksorganizer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.model.Event;

public class EventActivity extends AppCompatActivity {

    private EditText editTextName;
    private Spinner spinnerCategory;
    private EditText editTextDescription;
    private TextView textViewTime;
    private Button buttonSaveEvent;
    private Button buttonSetTime;

    private int CalendarHour, CalendarMinute;
    String format;
    Calendar calendar;
    TimePickerDialog timepickerdialog;

    //Добавить время
    //private EditText editTextTime?;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        init();
    }

    private void init() {

        editTextName = findViewById(R.id.editTextName);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextDescription = findViewById(R.id.editTextDescription);
        textViewTime = findViewById(R.id.textViewTime);
        buttonSaveEvent = findViewById(R.id.buttonSaveEvent);
        buttonSetTime = findViewById(R.id.buttonSetTime);



        buttonSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                eventPresenter.save();
            }
        });

        buttonSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);


                timepickerdialog = new TimePickerDialog(EventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                }
                                else if (hourOfDay == 12) {

                                    format = "PM";

                                }
                                else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                }
                                else {

                                    format = "AM";
                                }


                                textViewTime.setText(hourOfDay + ":" + minute + format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();

            }
        });

    }

    public Event getEvent() {
        String name = editTextName.getText().toString();
        Event.Category category = (Event.Category) spinnerCategory.getSelectedItem();
       /* Event.Category category;
        switch (categoryHUI){
            case MEETING:
                category = Event.Category.MEETING;
                break;
            case SPORT:
                category = Event.Category.SPORT;
                break;
            case ALKO:
                category = Event.Category.ALKO;
                break;
        }*/
        String description = editTextDescription.getText().toString();
        Long time = Long.parseLong(textViewTime.getText().toString());
        event = new Event(name, category, description, time);
        return event;
    }
}
