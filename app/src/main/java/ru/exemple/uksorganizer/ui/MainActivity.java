package ru.exemple.uksorganizer.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import ru.exemple.uksorganizer.App;
import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.model.Event;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, EventsListFragment.Listener {

    public final static String TAG = "myLOG";

    private EventsListFragment eventsListFragment;
    private ProgressBar progressBar;
    private EventsViewModel eventsViewModel;
    private DrawerLayout drawerLayout;
    private boolean isDeletedRequestFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventsViewModel.Factory factory = new EventsViewModel.Factory(
                ((App) getApplication()).getEventsDb());
        eventsViewModel = ViewModelProviders.of(this, factory).get(EventsViewModel.class);

        setContentView(R.layout.activity_main);
        eventsListFragment = (EventsListFragment) getSupportFragmentManager().findFragmentById(R.id.eventsListFragment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.nav_open_drawer, R.string.nav_close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        FloatingActionButton fab = findViewById(R.id.fab);
        progressBar = findViewById(R.id.pbMain);
        fab.setOnClickListener(this::addEvent);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState != null) {
            isDeletedRequestFlag = savedInstanceState.getBoolean("isDeletedRequestFlag");
        } else {
         //не совсем понятно зачем если потом в он старте все равно вызывается???
            eventsViewModel.load(isDeletedRequestFlag);
        }

        eventsViewModel.getLiveData().observe(this, new Observer<List<EventRow>>() {
            @Override
            public void onChanged(List<EventRow> eventRows) {
                MainActivity.this.onEventsLoaded(eventRows);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventsViewModel.load(isDeletedRequestFlag);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        EventsListFragment.LayoutManagerType layoutManagerType;
        switch (item.getItemId()) {
            case R.id.new_event_item:
                return true;
            case R.id.recycle_view_orientation_vertical_item:
                layoutManagerType = EventsListFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER_VERTICAL;
                eventsListFragment.setRecyclerViewManagerType(layoutManagerType);
                return true;
            case R.id.recycle_view_orientation_horizontal_item:
                layoutManagerType = EventsListFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER_HORIZONTAL;
                eventsListFragment.setRecyclerViewManagerType(layoutManagerType);
                return true;
            case R.id.recycle_view_orientation_grid_item:
                layoutManagerType = EventsListFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                eventsListFragment.setRecyclerViewManagerType(layoutManagerType);
                return true;
            case R.id.clear_trash_item:
                eventsViewModel.clearTrash(isDeletedRequestFlag);
            case R.id.settings_item:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("isDeletedRequestFlag", isDeletedRequestFlag);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_events:
                menuItem.setChecked(true);
                eventsViewModel.load(false);
                isDeletedRequestFlag = false;
                break;
            case R.id.nav_trash:
                menuItem.setChecked(true);
                eventsViewModel.load(true);
                isDeletedRequestFlag = true;
                break;
            case R.id.nav_search:
                menuItem.setChecked(true);
                break;
            case R.id.nav_help:
                break;
            case R.id.nav_feedback:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void addEvent(View view) {
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    private void checkEmptyList(List<EventRow> events) {
        if (events.size() == 0) {
            findViewById(R.id.tvEmpty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.tvEmpty).setVisibility(View.GONE);
        }
    }

    private void openDeleteDialog(Event event) {
        final Event eventInner = event;
        AlertDialog.Builder deleteEventDialog = new AlertDialog.Builder(this);
        deleteEventDialog.setTitle(R.string.delete_event_question);
        deleteEventDialog.setPositiveButton(R.string.ok, (dialog, which) -> deleteEvent(eventInner));
        deleteEventDialog.setNegativeButton(R.string.cancel, null);
        deleteEventDialog.create();
        deleteEventDialog.show();
    }

    private void deleteEvent(Event event) {
        progressBar.setVisibility(View.VISIBLE);
        eventsViewModel.delete(event, isDeletedRequestFlag);
    }

    private void onEventsLoaded(List<EventRow> eventRows) {
        eventsListFragment.initData(eventRows);
        progressBar.setVisibility(View.INVISIBLE);
        checkEmptyList(eventRows);
    }

    @Override
    public void onEventListFragmentItemClick(Event event) {
        EventActivity.start(this, event);
    }

    @Override
    public void onEventListFragmentItemLongClick(Event event) {
        openDeleteDialog(event);
    }
}