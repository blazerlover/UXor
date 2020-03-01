package ru.exemple.uksorganizer.ui;

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

    private final List<EventRow> events;
    private final Listener listener;

    public EventsAdapter(List<EventRow> events, Listener listener) {
        this.events = events;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventRow eventRow = events.get(position);
        holder.tvTitle.setText(eventRow.title);
        holder.tvCategory.setText(eventRow.category);
        holder.tvTime.setText(eventRow.time);
        holder.ivCategory.setImageResource(eventRow.image);
        holder.itemView.setOnClickListener(v -> listener.onEventClick(eventRow.event));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onEventLongClick(eventRow.event);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
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

    public interface Listener {

        void onEventClick(Event event);

        void onEventLongClick(Event event);

    }

}
