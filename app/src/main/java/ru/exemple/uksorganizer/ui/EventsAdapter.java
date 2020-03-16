package ru.exemple.uksorganizer.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.model.Event;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    public final static String TAG = "myLOG";

    private final List<EventRow> eventRows;
    private final Listener listener;
    private View view;

    public EventsAdapter(List<EventRow> eventRows, Listener listener) {
        this.eventRows = eventRows;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.row_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventRow eventRow = eventRows.get(position);
        holder.tvTitle.setText(eventRow.title);
        holder.tvCategory.setText(eventRow.category);
        holder.tvTime.setText(eventRow.time);
        holder.ivCategory.setImageResource(eventRow.image);
        try {
            view.setBackgroundColor(view.getResources().getColor(eventRow.priority));
        } catch (Exception e) {
            view.setBackgroundColor(view.getResources().getColor(R.color.colorCategorySomething));
        }
        holder.itemView.setOnClickListener(v -> listener.onEventClick(eventRow.event));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onEventLongClick(eventRow.event);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return eventRows.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvCategory;
        private final TextView tvTime;
        private final ImageView ivCategory;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvEventTitle);
            tvCategory = itemView.findViewById(R.id.tvEventCategory);
            tvTime = itemView.findViewById(R.id.tvEventTime);
            ivCategory = itemView.findViewById(R.id.ivCategory);
        }

    }

    public void setEventRows(List<EventRow> eventRows) {
        for (EventRow eventRow:eventRows) {
            Log.d(TAG, "priority = " + eventRow.priority);
        }
        this.eventRows.clear();
        this.eventRows.addAll(eventRows);
        notifyDataSetChanged();
    }

    public interface Listener {

        void onEventClick(Event event);

        void onEventLongClick(Event event);

    }

}
