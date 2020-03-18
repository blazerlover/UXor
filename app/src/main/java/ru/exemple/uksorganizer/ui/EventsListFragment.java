package ru.exemple.uksorganizer.ui;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.model.Event;

public class EventsListFragment extends Fragment implements EventsAdapter.Listener {

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private Listener listener;
    private RecyclerView recyclerView;
    private LayoutManagerType currentLayoutManagerType;
    private List<EventRow> eventRows;
    private EventsAdapter adapter;

    enum LayoutManagerType {
        LINEAR_LAYOUT_MANAGER_VERTICAL,
        LINEAR_LAYOUT_MANAGER_HORIZONTAL,
        GRID_LAYOUT_MANAGER
    }

    public EventsListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);
        eventRows = new ArrayList<>();
        adapter = new EventsAdapter(eventRows, this);
        recyclerView = view.findViewById(R.id.rvEvents);
        recyclerView.setAdapter(adapter);
        currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER_VERTICAL;
        if (savedInstanceState != null) {
            currentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewManagerType(currentLayoutManagerType);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_LAYOUT_MANAGER, currentLayoutManagerType);
    }

    @Override
    public void onEventClick(Event event) {
        listener.onEventListFragmentItemClick(event);
    }

    @Override
    public void onEventLongClick(Event event) {
        listener.onEventListFragmentItemLongClick(event);
    }

    public void initData(List<EventRow> eventRows) {
        this.eventRows = eventRows;
        adapter.setEventRows(eventRows);
    }

    public void setRecyclerViewManagerType(LayoutManagerType layoutManagerType) {
        DividerItemDecoration dividerItemDecoration;
        LinearLayoutManager linearLayoutManager;
        switch (layoutManagerType) {
            case LINEAR_LAYOUT_MANAGER_VERTICAL:
                linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                        linearLayoutManager.getOrientation());
                dividerItemDecoration.setOrientation(RecyclerView.VERTICAL);
                this.currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER_VERTICAL;
                break;
            case LINEAR_LAYOUT_MANAGER_HORIZONTAL:
                linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                        linearLayoutManager.getOrientation());
                dividerItemDecoration.setOrientation(RecyclerView.HORIZONTAL);
                this.currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER_VERTICAL;
                break;
            case GRID_LAYOUT_MANAGER:
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                this.currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
        }
    }

    public interface Listener {
        void onEventListFragmentItemClick(Event event);

        void onEventListFragmentItemLongClick(Event event);
    }
}
