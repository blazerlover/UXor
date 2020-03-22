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
        view = layoutInflater.inflate(R.layout.row_event_second_edition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventRow eventRow = eventRows.get(position);
        holder.tvTitle.setText(eventRow.title);
        holder.ivCategorySecondEdition.setImageResource(eventRow.category);
        holder.tvTime.setText(eventRow.time);
        holder.ivCategoryBackGround.setImageResource(eventRow.categoryBackground);
        holder.ivPriority.setImageResource(eventRow.priority);
        //holder.ivPriority.setColorFilter(R.color.colorPriorityHard);

        try {
            holder.ivPriority.setColorFilter(holder.itemView.getResources().getColor(eventRow.priorityBackground));
        } catch (Exception e) {
            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorPriorityLow));
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
        private final ImageView ivPriority;
        private final TextView tvTime;
        private final ImageView ivCategorySecondEdition;
        private final ImageView ivCategoryBackGround;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvEventTitle);
            ivCategorySecondEdition = itemView.findViewById(R.id.ivCategorySecondEdition);
            ivPriority = itemView.findViewById(R.id.ivPriority);
            tvTime = itemView.findViewById(R.id.tvEventTime);
            ivCategoryBackGround = itemView.findViewById(R.id.ivCategoryBackground);
        }

    }

    /*static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvCategory;
        private final TextView tvTime;
        private final ImageView ivCategorySecondEdition;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvEventTitle);
            tvCategory = itemView.findViewById(R.id.tvEventCategory);
            tvTime = itemView.findViewById(R.id.tvEventTime);
            ivCategorySecondEdition = itemView.findViewById(R.id.ivCategorySecondEdition);
        }

    }*/

    public void setEventRows(List<EventRow> eventRows) {
        this.eventRows.clear();
        this.eventRows.addAll(eventRows);
        notifyDataSetChanged();
    }

    public interface Listener {

        void onEventClick(Event event);

        void onEventLongClick(Event event);

    }

}
