package ru.exemple.uksorganizer.ui;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.model.Event;


public class EventDetailReadOnlyFragment extends Fragment {

    private static final String EXTRA_EVENT = "EVENT";

    private Listener listener;
    private TextView editTextName, editTextDescription;
    private Spinner spinnerCategory, spinnerPriority;
    private TextView textViewTime;
    private TextView textViewDate;
    private Calendar calendar = Calendar.getInstance();

    private Event event;
    private Event.Category[] categoriesArray = Event.Category.values();
    private String[] priorityArray;
    private final static String TAG = EventActivity.class.getName();

    public EventDetailReadOnlyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_detail_read_only, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        View viewFrag = getView();
        if (viewFrag != null) {
            editTextName = viewFrag.findViewById(R.id.editTextName);
            spinnerCategory = viewFrag.findViewById(R.id.spinnerCategory);
            spinnerPriority = viewFrag.findViewById(R.id.spinnerPriority);
            editTextDescription = viewFrag.findViewById(R.id.editTextDescription);
            textViewTime = viewFrag.findViewById(R.id.textViewTime);
            textViewDate = viewFrag.findViewById(R.id.textViewDate);
            FloatingActionButton imageButton = viewFrag.findViewById(R.id.editEventButton);
            imageButton.setOnClickListener(v -> listener.onEditButtonClicked());
            priorityArray = getActivity().getResources().getStringArray(R.array.priority);

            ArrayAdapter<Event.Category> arrayAdapter = new ArrayAdapter<Event.Category>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, categoriesArray);
            spinnerCategory.setEnabled(false);
            spinnerCategory.setClickable(false);
            spinnerCategory.setAdapter(arrayAdapter);

            ArrayAdapter<String> arrayPriorityAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, priorityArray);
            spinnerPriority.setEnabled(false);
            spinnerPriority.setClickable(false);
            spinnerPriority.setAdapter(arrayPriorityAdapter);

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
        String priorityPos = spinnerPriority.getSelectedItem().toString();
        switch (priorityPos) {
            case "Low priority":
                priority = 0;
                break;
            case "Middle priority":
                priority = 1;
                break;
            case "High priority":
                priority = 2;
                break;
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
            Bundle bundle = this.getArguments();
            event = (Event) bundle.getSerializable(EXTRA_EVENT);
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

        ArrayAdapter<String> arrayPriorityAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, priorityArray);
        spinnerPriority.setAdapter(arrayPriorityAdapter);
        int priorityPosition = 0;
        switch (event.getPriority()) {
            case 0:
                priorityPosition = 0;
                break;
            case 1:
                priorityPosition = 1;
                break;
            case 2:
                priorityPosition = 2;
                break;
        }
        spinnerPriority.setSelection(priorityPosition);
    }

    boolean eventChanged() {
        Event newEvent = this.getEvent();
        return !event.equals(newEvent);
    }

    interface Listener {
        void onEditButtonClicked();
    }
}
