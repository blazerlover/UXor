package ru.exemple.uksorganizer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import ru.exemple.uksorganizer.App;
import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.db.EventsDatabase;
import ru.exemple.uksorganizer.model.Event;

//TODO: сделать чтобы можно было выбирать setLayoutManager recycler из UI
//TODO: сделть чтобы если нет events - отображалась вьюшка с текстом "Еще нет евентиов, добавьте"
//TODO: сделать загрузку данных асинхронно (в другом потоке), пока грузится выводить прогресс
public class MainActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskListener {

    private RecyclerView recycler;
    private ProgressBar progressBar;
    private ArrayList<Event> events;
    private DividerItemDecoration dividerItemDecoration;
    private LinearLayoutManager llManager;
    private DataLoader dataLoader;
    private int currentOrientation;
    private DataStateStorage storage;

    final static String TAG = "myLOG";
    EventsDatabase eventsDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventsDb = ((App) getApplication()).getEventsDb();

        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        progressBar = findViewById(R.id.pbMain);
        fab.setOnClickListener(this);
        recycler = findViewById(R.id.rvEvents);
        llManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(recycler.getContext(),
                llManager.getOrientation());

        if (savedInstanceState != null) {
            currentOrientation = savedInstanceState.getInt("currentOrientation");
            setCurrentOrientation(currentOrientation);
        }
        else
            recycler.setLayoutManager(llManager);

        storage = (DataStateStorage) getLastCustomNonConfigurationInstance();
        if (storage == null) {
            Log.d(TAG, "storage == null");
            storage = new DataStateStorage(dataLoader, events);
        }
        events = storage.getList();
        if (events == null)
            Log.d(TAG, "events == null");
        if (events != null) {
            Log.d(TAG, "events != null");
            onAsyncTaskFinished(events);
        }
        dataLoader = storage.getDataLoader();
        if (dataLoader == null) {
            Log.d(TAG, "dataLoader == null");
            dataLoader = new DataLoader(this);
            dataLoader.execute(events);
        }

    }

    @Override
    public void onAsyncTaskFinished(ArrayList<Event> events) {
        EventsAdapter eventsAdapter = new EventsAdapter(events);
        recycler.setAdapter(eventsAdapter);
        recycler.addItemDecoration(dividerItemDecoration);
        checkEmptyList(events);
        storage.saveDataState(this.dataLoader, events);
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_event_item:
                return true;
            case R.id.recycle_view_orientation_vertical_item:
                onVerticalOrientation();
                return true;
            case R.id.recycle_view_orientation_horizontal_item:
                onHorizontalOrientation();
                return true;
            case R.id.recycle_view_orientation_grid_item:
                onGridOrientation();
                return true;
            case R.id.settings_item:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("currentOrientation", currentOrientation);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return storage;
    }

    public void checkEmptyList(ArrayList<Event> events) {
        if (events.size() == 0)
            findViewById(R.id.tvEmpty).setVisibility(View.VISIBLE);
    }

    public void onVerticalOrientation() {
        llManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recycler.setLayoutManager(llManager);
        dividerItemDecoration.setOrientation(RecyclerView.VERTICAL);
        currentOrientation = 0;
    }

    public void onHorizontalOrientation() {
        llManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recycler.setLayoutManager(llManager);
        dividerItemDecoration.setOrientation(RecyclerView.HORIZONTAL);
        currentOrientation = 1;
    }

    public void onGridOrientation() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recycler.setLayoutManager(gridLayoutManager);
        currentOrientation = 2;
    }

    public void setCurrentOrientation(int currentOrientation) {
        switch (currentOrientation) {
            case 0:
                onVerticalOrientation();
                break;
            case 1:
                onHorizontalOrientation();
                break;
            case 2:
                onGridOrientation();
                break;
        }

    }

    class DataLoader extends AsyncTask<ArrayList<Event>, Integer, ArrayList<Event>> {

        MainActivity activity;

        DataLoader(MainActivity activity){
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Data loading", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected ArrayList<Event> doInBackground(ArrayList<Event>... events) {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            Log.d(TAG, "activity hash" + activity.hashCode() + " task hash" + dataLoader.hashCode());
            events[0] = (ArrayList<Event>) eventsDb.getAllEvents();
            return events[0];
        }

        @Override
        protected void onPostExecute(ArrayList<Event> events) {
            super.onPostExecute(events);
            progressBar.setVisibility(View.GONE);
            activity.onAsyncTaskFinished(events);
            Toast.makeText(MainActivity.this, "Data loaded", Toast.LENGTH_SHORT).show();
        }

    }

}