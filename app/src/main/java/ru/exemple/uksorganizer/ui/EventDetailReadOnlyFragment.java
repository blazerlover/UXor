package ru.exemple.uksorganizer.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.model.Event;


public class EventDetailReadOnlyFragment extends Fragment {

    private int eventID;

    private static final String EXTRA_EVENT = "EVENT";

    private EditText editTextName, editTextDescription;
    private Spinner spinnerCategory;
    private TextView textViewTime;
    private TextView textViewDate;
    private Button buttonSaveEvent;
    private CheckBox checkBox;
    private Calendar calendar = Calendar.getInstance();

    private Event event;
    private EventsDatabase eventsDatabase;
    private EventDetailFragment.Listener listener;
    private Event.Category [] categoriesArray = Event.Category.values();
    private final static String TAG = EventActivity.class.getName();

    public EventDetailReadOnlyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_detail_read_only, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View viewFrag = getView();
        if (viewFrag != null) {
            editTextName = viewFrag.findViewById(R.id.editTextName);
            spinnerCategory = viewFrag.findViewById(R.id.spinnerCategory);
            editTextDescription = viewFrag.findViewById(R.id.editTextDescription);
            textViewTime = viewFrag.findViewById(R.id.textViewTime);
            textViewDate = viewFrag.findViewById(R.id.textViewDate);
            buttonSaveEvent = viewFrag.findViewById(R.id.buttonSaveEvent);
            checkBox = viewFrag.findViewById(R.id.priority);

            ArrayAdapter<Event.Category> arrayAdapter = new ArrayAdapter<Event.Category>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, categoriesArray);
            spinnerCategory.setAdapter(arrayAdapter);

            setInitialDateTime();
            getIntentFromMain();
        }
    }

    public Event getEvent() {
        String name = editTextName.getText().toString();
        Event.Category category = (Event.Category) spinnerCategory.getSelectedItem();
        String description = editTextDescription.getText().toString();
        long time = calendar.getTimeInMillis();
        int priority = 0;
        if (checkBox.isChecked()) {
            priority = 1;
        }
        return new Event(name, category, description, time, priority);
    }

    private void setInitialDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        SimpleDateFormat stf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        textViewDate.setText(sdf.format(calendar.getTimeInMillis()));
        textViewTime.setText(stf.format(calendar.getTimeInMillis()));
    }

    private void getIntentFromMain() {
        event = (Event) getActivity().getIntent().getSerializableExtra(EXTRA_EVENT);
        if (event == null) {
            event = new Event("", Event.Category.SOMETHING, "", System.currentTimeMillis(), 0);
        }
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getTime());

        editTextName.setText(event.getName());

        Event.Category category = event.getCategory();
        ArrayAdapter<Event.Category> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categoriesArray);
        spinnerCategory.setAdapter(arrayAdapter);
        int position = arrayAdapter.getPosition(category);
        spinnerCategory.setSelection(position);

        editTextDescription.setText(event.getDescription());
        SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        SimpleDateFormat tf = new SimpleDateFormat("hh:mm", Locale.getDefault());
        textViewDate.setText(df.format(event.getTime()));
        textViewTime.setText(tf.format(event.getTime()));
        boolean checked = false;
        if (event.getPriority() == 1) {
            checked = true;
        }
        checkBox.setChecked(checked);
    }

    void setEventID(int id) {
        this.eventID = id;
    }
}
