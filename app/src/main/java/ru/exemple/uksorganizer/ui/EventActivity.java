package ru.exemple.uksorganizer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import ru.exemple.uksorganizer.R;

public class EventActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextCategory;
    private EditText editTextDescription;

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

//        getEventData();

        findViewById(R.id.buttonSaveEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                eventPresenter.save();
            }
        });


    }

//    public Event getEventData() {
//        Event eventData = new Event(name, category, description, time);
//        eventData.setName(editTextName.getText().toString());
//        eventData.setCategory(editTextCategory.getText().toString());
//        eventData.setDescription(editTextDescription.getText().toString());
//        return eventData;
//    }
}
