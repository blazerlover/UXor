package ru.exemple.uksorganizer.ui;

import java.util.Comparator;

import ru.exemple.uksorganizer.model.Event;

public class EventRow {

    public final String title;
    public final String time;
    public final int category;
    public final int categoryBackground;
    public final int priority;
    public final int priorityBackground;
    public final Event event;

    public EventRow(String title, int category, String time, int categoryBackground, int priority, int priorityBackground, Event event) {
        this.title = title;
        this.category = category;
        this.time = time;
        this.categoryBackground = categoryBackground;
        this.priority = priority;
        this.priorityBackground = priorityBackground;
        this.event = event;
    }

    public static class PriorityComparator implements Comparator<EventRow> {
        @Override
        public int compare(EventRow o1, EventRow o2) {
            return o2.priority - o1.priority;
        }
        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }
    public static class TitleComparator implements Comparator<EventRow> {
        @Override
        public int compare(EventRow o1, EventRow o2) {
            return o1.title.compareTo(o2.title);
        }
        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }
    public static class TimeComparator implements Comparator<EventRow> {
        @Override
        public int compare(EventRow o1, EventRow o2) {
            return (int)(o1.event.getTime() - o2.event.getTime());
        }
        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }
}
