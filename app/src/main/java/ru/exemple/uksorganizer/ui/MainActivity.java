package ru.exemple.uksorganizer.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ru.exemple.uksorganizer.App;
import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.model.Event;

public class MainActivity extends AppCompatActivity implements EventsAdapter.Listener{

    private RecyclerView recycler;
    private ProgressBar progressBar;
    private DividerItemDecoration dividerItemDecoration;
    private LinearLayoutManager llManager;
    private EventsViewModel eventsViewModel;
    private int rvManagerType;

    final static String TAG = "myLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventsViewModel.Factory factory = new EventsViewModel.Factory(
                ((App) getApplication()).getEventsDb());
        eventsViewModel = ViewModelProviders.of(this, factory).get(EventsViewModel.class);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        FloatingActionButton fab = findViewById(R.id.fab);
        progressBar = findViewById(R.id.pbMain);
        View.OnClickListener listener = v -> addEvent(v);
        fab.setOnClickListener(listener);
        recycler = findViewById(R.id.rvEvents);
        llManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(recycler.getContext(),

                llManager.getOrientation());

        if (savedInstanceState != null) {
            rvManagerType = savedInstanceState.getInt("rvManagerType");
            setCurrentOrientation(rvManagerType);
        } else {
            recycler.setLayoutManager(llManager);
        }

        eventsViewModel.getLiveData().observe(this, this::onEventsLoaded);
        //не совсем понятно зачем если потом в он старте все равно вызывается???
        if (savedInstanceState == null) {
            eventsViewModel.load();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventsViewModel.load();
    }

    public void addEvent(View view){
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
        savedInstanceState.putInt("rvManagerType", rvManagerType);
    }

    public void checkEmptyList(List<EventRow> events) {
        if (events.size() == 0) {
            findViewById(R.id.tvEmpty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.tvEmpty).setVisibility(View.GONE);
        }
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

    public void onVerticalOrientation() {
        llManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recycler.setLayoutManager(llManager);
        dividerItemDecoration.setOrientation(RecyclerView.VERTICAL);
        rvManagerType = 0;
    }

    public void onHorizontalOrientation() {
        llManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recycler.setLayoutManager(llManager);
        dividerItemDecoration.setOrientation(RecyclerView.HORIZONTAL);
        rvManagerType = 1;
    }

    public void onGridOrientation() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recycler.setLayoutManager(gridLayoutManager);
        rvManagerType = 2;
    }

    public void openDeleteDialog(Event event) {
        final Event eventInner = event;
        AlertDialog.Builder deleteEventDialog = new AlertDialog.Builder(this);
        deleteEventDialog.setTitle(R.string.delete_event_q);
        deleteEventDialog.setPositiveButton(R.string.ok, (dialog, which) -> updateDB(eventInner));
        deleteEventDialog.setNegativeButton(R.string.cancel, null);
        deleteEventDialog.create();
        deleteEventDialog.show();
    }

    public void updateDB(Event event) {
        progressBar.setVisibility(View.VISIBLE);
        eventsViewModel.delete(event);
    }

    @Override
    public void onEventClick(Event event) {
        EventActivity.start(this, event);
    }

    @Override
    public void onEventLongClick(Event event) {
        openDeleteDialog(event);
    }

    public void onEventsLoaded(List<EventRow> eventRows) {
        EventsAdapter eventsAdapter = new EventsAdapter(eventRows, this);
        recycler.setAdapter(eventsAdapter);
        recycler.addItemDecoration(dividerItemDecoration);
        progressBar.setVisibility(View.INVISIBLE);
        checkEmptyList(eventRows);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}