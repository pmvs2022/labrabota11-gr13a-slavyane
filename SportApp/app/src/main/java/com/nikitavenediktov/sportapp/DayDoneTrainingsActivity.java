package com.nikitavenediktov.sportapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DayDoneTrainingsActivity extends AppCompatActivity {

    public static final SimpleDateFormat DATE_FORMAT_DAY =
            new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_TIME =
            new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    private Date dayDate;
    private ArrayList<DoneTraining> doneTrainings_day;
    private DayDoneTrainingsAdapter adapter;

    TextView dayDate_tv;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_trainings);

        dayDate_tv = (TextView) findViewById(R.id.day_date_tv);
        recyclerView = (RecyclerView) findViewById(R.id.done_trainings);

        Intent intent = getIntent();
        String date_str = intent.getStringExtra("date");
        try {
            dayDate = CalendarFragment.DATE_FORMAT_DAY.parse(date_str);
        }
        catch (ParseException ex) {
            System.err.println(ex.getMessage());
        }

        doneTrainings_day = SportDbHelper.getInstance(this).getDoneTrainings(date_str);
        adapter = new DayDoneTrainingsAdapter(DayDoneTrainingsActivity.this, doneTrainings_day);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DayDoneTrainingsActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        dayDate_tv.setText(DATE_FORMAT_DAY.format(dayDate));
    }
}
