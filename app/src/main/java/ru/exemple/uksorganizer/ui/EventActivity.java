package ru.exemple.uksorganizer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.model.Event;

public class EventActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextCategory;
    private EditText editTextDescription;

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

        editTextName = findViewById(R.id.edittextName);
        editTextCategory = findViewById(R.id.edittextCategoty);
        editTextDescription = findViewById(R.id.edittextDescription);
        //editTextTime = findViewById(R.id.edittextTime);

//        getEvent();

        findViewById(R.id.buttonSaveEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                eventPresenter.save();
            }
        });


    }

    public Event getEvent() {
        String name = editTextName.getText().toString();
        Event.Category category;
        String description = editTextDescription.getText().toString();
        //Long time = editTextTime.
        event = new Event(name, category, description, time);
        return event;
    }
}
