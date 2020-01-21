package ru.exemple.uksorganizer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.exemple.uksorganizer.App;
import ru.exemple.uksorganizer.R;
import ru.exemple.uksorganizer.db.EventsDatabase;

//TODO: сделать чтобы можно было выбирать setLayoutManager recycler из UI
//TODO: сделть чтобы если нет events - отображалась вьюшка с текстом "Еще нет евентиов, добавьте"
//TODO: сделать загрузку данных асинхронно (в другом потоке), пока грузится выводить прогресс
 public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventsDatabase eventsDb = ((App) getApplication()).getEventsDb();

        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        RecyclerView recycler = findViewById(R.id.rvEvents);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        EventsAdapter eventsAdapter = new EventsAdapter(eventsDb.getAllEvents());
        recycler.setAdapter(eventsAdapter);
    }

    //Пока что слушатель только у одной кнопки, потом нужно будет пердусмотреть оператор switch/case или прикручивать к каждой кнопке отдельно
    @Override
    public void onClick(View view){
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu_main, menu);
        return true;
    }
}
