package ru.exemple.uksorganizer.ui;

import java.util.ArrayList;

import ru.exemple.uksorganizer.model.Event;

public class DataStateStorage {

    private MainActivity.DataLoader dataLoader;
    private ArrayList<Event> list;

    DataStateStorage(MainActivity.DataLoader dataLoader, ArrayList<Event> list) {
        this.dataLoader = dataLoader;
        this.list = list;
    }

    public MainActivity.DataLoader getDataLoader() {
        return dataLoader;
    }

    public ArrayList<Event> getList() {
        return list;
    }

    public void saveDataState(MainActivity.DataLoader dataLoader, ArrayList<Event> list) {
        this.dataLoader = dataLoader;
        this.list = list;
    }
}
