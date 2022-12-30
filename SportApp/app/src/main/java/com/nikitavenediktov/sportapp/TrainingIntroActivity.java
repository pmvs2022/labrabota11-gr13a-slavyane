package com.nikitavenediktov.sportapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrainingIntroActivity extends AppCompatActivity implements View.OnClickListener{
    private String training_id;
    private ArrayList<Exercise> exercises;
    private TrainingIntroAdapter adapter;

    Button start_btn;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_intro);

        start_btn = (Button) findViewById(R.id.start_btn);
        recyclerView = (RecyclerView) findViewById(R.id.exercises);

        Intent intent = getIntent();
        training_id = intent.getStringExtra("training_id");

        setTitle(getTitle() + " #" + training_id);

        exercises = SportDbHelper.getInstance(this)
                .getTrainingExercises(Integer.parseInt(training_id));

        adapter = new TrainingIntroAdapter(TrainingIntroActivity.this, training_id, exercises);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TrainingIntroActivity.this));

        start_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {

        Intent intent = new Intent(TrainingIntroActivity.this, ExercisesPipelineActivity.class);

        intent.putExtra("training_id", training_id);
        startActivity(intent);
    }

}
