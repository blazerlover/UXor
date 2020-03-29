package ru.exemple.uksorganizer.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.model.Event;


public class EventDetailReadOnlyFragment extends Fragment {

    private static final String EXTRA_EVENT = "EVENT";

    private TextView editTextName, editTextDescription;
    private Spinner spinnerCategory;
    private TextView textViewTime;
    private TextView textViewDate;
    private CheckBox checkBox;
    private Calendar calendar = Calendar.getInstance();

    private Event event;
    private Event.Category [] categoriesArray = Event.Category.values();
    private final static String TAG = EventActivity.class.getName();

    public EventDetailReadOnlyFragment() {
    }

    public EventDetailReadOnlyFragment(Event event) {

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
            checkBox = viewFrag.findViewById(R.id.priority);

            ArrayAdapter<Event.Category> arrayAdapter = new ArrayAdapter<Event.Category>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, categoriesArray);
            spinnerCategory.setEnabled(false);
            spinnerCategory.setClickable(false);
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

    public void getIntentFromMain() {
        event = (Event) getActivity().getIntent().getSerializableExtra(EXTRA_EVENT);
        if (event == null) {
            Bundle bundle = this.getArguments();
            event = (Event) bundle.getSerializable(EXTRA_EVENT);
            //event = new Event("", Event.Category.SOMETHING, "", System.currentTimeMillis(), 0);
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
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        textViewDate.setText(df.format(event.getTime()));
        textViewTime.setText(tf.format(event.getTime()));
        boolean checked = false;
        if (event.getPriority() == 1) {
            checked = true;
        }
        checkBox.setChecked(checked);
    }

    boolean eventChanged() {
        Event newEvent = this.getEvent();
        return !event.equals(newEvent);
    }
}
