package ru.exemple.uksorganizer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EventActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextCategory;
    private EditText editTextDescription;
    private EventPresenter eventPresenter;

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

        getEventData();

        findViewById(R.id.buttonSaveEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventPresenter.save();
            }
        });


    }

    public EventData getEventData() {
        EventData eventData = new EventData();
        eventData.setName(editTextName.getText().toString());
        eventData.setCategory(editTextCategory.getText().toString());
        eventData.setDescription(editTextDescription.getText().toString());
        return eventData;
    }
}
