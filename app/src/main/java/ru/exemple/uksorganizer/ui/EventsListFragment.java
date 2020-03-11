package ru.exemple.uksorganizer.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.exemple.uksorganizer.R;

public class EventsListFragment extends Fragment {


    public EventsListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_list, container, false);


    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView recyclerView = getView().findViewById(R.id.rvEvents);
    }
}
