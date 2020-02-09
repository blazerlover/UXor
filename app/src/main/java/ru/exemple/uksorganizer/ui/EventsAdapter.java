package ru.exemple.uksorganizer.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.model.Event;

//TODO: отобразить остальные данные из Event
//TODO: добавить разделитель между элементами
//TODO: отображать категории в виде иконок в кружочке, каждая категория - свой цвет круга (см пример GMail)
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private final List<Event> events;

    public EventsAdapter(List<Event> events) {
        this.events = events;
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
        Event event = events.get(position);
        holder.tvTitle.setText(event.getName());
        holder.tvCategory.setText(event.getCategory().toString());
        Date date = new Date();
        date.setTime(event.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String time = String.format("%tb %te %tR", calendar, calendar, calendar);
        holder.tvTime.setText(time);
        switch (event.getCategory()) {
            case ALKO:
                holder.ivCategory.setImageResource(R.drawable.category_alko_shape);
                break;
            case MEETING:
                holder.ivCategory.setImageResource(R.drawable.category_meeting_shape);
                break;
            case SPORT:
                holder.ivCategory.setImageResource(R.drawable.category_sport_shape);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvCategory;
        private final TextView tvTime;
        private final ImageView ivCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvEventTitle);
            tvCategory = itemView.findViewById(R.id.tvEventCategory);
            tvTime = itemView.findViewById(R.id.tvEventTime);
            ivCategory = itemView.findViewById(R.id.ivCategory);
        }

    }

}
