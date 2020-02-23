package ru.exemple.uksorganizer.ui;

import android.content.Context;
import android.content.Intent;
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
    private View view;
    private MainActivity mainActivity;

    public EventsAdapter(List<Event> events, Context context) {
        this.events = events;
        this.mainActivity = (MainActivity) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.row_event, parent, false);
        return new ViewHolder(view);
    }

    @Override

    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Event event = events.get(position);
        final String name = event.getName();
        final Event.Category category = event.getCategory();
        final String description = event.getDescription();
        final long time = event.getTime();
        holder.tvTitle.setText(name);
        holder.tvCategory.setText(category.toString());
        Date date = new Date();
        date.setTime(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String strTime = String.format("%tb %te %tR", calendar, calendar, calendar);
        holder.tvTime.setText(strTime);
        switch(category) {

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
        //вызов EventActivity для редактирования при нажатии на Event в RecyclerView
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), EventActivity.class);
                //Имеет ли смысл передавать в массиве стринговом?
                intent.putExtra("name", name);
                intent.putExtra("category", category.toString());
                intent.putExtra("description", description);
                intent.putExtra("time", time);
                view.getContext().startActivity(intent);
            }
        });
        //удаление Event из RecyclerView при долгом нажатии на него
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*events.remove(holder.getLayoutPosition());
                notifyDataSetChanged();*/
                Event event = events.get(holder.getLayoutPosition());
                mainActivity.onAdapterDataChanged(event);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

 /*   @Override
    public List<Event> onDataChanged(List<Event> e) {
        return null;
    }*/

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
