package ru.exemple.uksorganizer.ui;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import ru.exemple.uksorganizer.App;
import ru.exemple.uksorganizer.EventNotification.EventNotification;
import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.model.Event;


public class EventDetailFragment extends Fragment {

    private int eventID;

    private static final String EXTRA_EVENT = "EVENT";

    private EditText editTextName, editTextDescription;
    private Spinner spinnerCategory, spinnerPriority;
    private TextView textViewTime;
    private TextView textViewDate;
    private Button buttonSaveEvent;
    //private CheckBox checkBox;
    private Calendar calendar = Calendar.getInstance();
    private TimePickerDialog timepickerdialog;
    private DatePickerDialog datePickerDialog;

    private Event event;
    private EventsDatabase eventsDatabase;
    private Listener listener;
    private Event.Category [] categoriesArray = Event.Category.values();
    private String [] priorityArray;
    private final static String TAG = EventActivity.class.getName();

       public EventDetailFragment() {
    }

    interface Listener {
        void itemClicked(long id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsDatabase = ((App) getActivity().getApplication()).getEventsDb();

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
            buttonSaveEvent = viewFrag.findViewById(R.id.buttonSaveEvent);

            //checkBox = viewFrag.findViewById(R.id.priority);

            priorityArray = getActivity().getResources().getStringArray(R.array.priority);
            ArrayAdapter<Event.Category> arrayAdapter = new ArrayAdapter<Event.Category>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, categoriesArray);
            spinnerCategory.setAdapter(arrayAdapter);
            ArrayAdapter <String> arrayPriorityAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, priorityArray);
            spinnerPriority.setAdapter(arrayPriorityAdapter);

            textViewDate.setOnClickListener(v -> {
                datePickerDialog = new DatePickerDialog(getActivity(), (view, year, month, day) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    setInitialDateTime();
                }, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            });

            textViewTime.setOnClickListener(v -> {
                timepickerdialog = new TimePickerDialog(getActivity(),
                        (view, hourOfDay, minute) -> {
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);
                            setInitialDateTime();
                        }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
                timepickerdialog.show();
            });

            buttonSaveEvent.setOnClickListener(v -> {
                Event event = this.getEvent();
                if (event.getName().length() == 0) {
                    openEnterNameDialog();
                } else {
                    //EventActivity.this.getEvent() - надо вынести это в переменную,
                    //в оповещении используется это значение
                    eventsDatabase.addEvent((event));
                    //мой код по оповещению:
                    //правильно ли создавать здесь экземпляр?
                    EventNotification notification = new EventNotification(getContext(), event);
                    notification.createNotificationChannel();
                    notification.createWorkNotification();
                    getActivity().finish();
                }
            });

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
            case "Low priority": priority = 0;
                 break;
            case "Middle priority": priority = 1;
                break;
            case "High priority": priority = 2;
                break;
        }
        return new Event(name, category, description, time, priority);
    }

    private void setInitialDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm", Locale.getDefault());
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
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        textViewDate.setText(df.format(event.getTime()));
        textViewTime.setText(tf.format(event.getTime()));
       /* boolean checked = false;
        if (event.getPriority() == 1) {
            checked = true;
        }
        checkBox.setChecked(checked);*/

        ArrayAdapter <String> arrayPriorityAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, priorityArray);
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

    private void openEnterNameDialog () {
        AlertDialog.Builder nameDialog = new AlertDialog.Builder(getActivity());
        nameDialog.setTitle(R.string.enter_name);
        nameDialog.setNegativeButton(R.string.ok, null);
        nameDialog.create();
        nameDialog.show();
    }

    boolean eventChanged() {
        Event newEvent = this.getEvent();
        return !event.equals(newEvent);
    }
}
