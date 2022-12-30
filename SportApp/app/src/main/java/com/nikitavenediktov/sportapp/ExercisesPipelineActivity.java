package com.nikitavenediktov.sportapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class ExercisesPipelineActivity extends AppCompatActivity implements View.OnClickListener {
    private static final long REST_TIME = 15; // seconds
    private long start, finish;
    private String training_id;
    private ArrayList<Exercise> exercises;
    private int exercise_index;
    private boolean isRest;
    private LocalDateTime start_date;

    TextView progress_tv, exercise_title, times_tv, time_tv, next_exercise_tv, next_exercise_sign_tv;
    Button cont_button;
    ProgressBar time_pb;
    GifImageView action_gif;

    CountDownTimer exercise_timer;
    MediaPlayer tick_player, end_player, victory_player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise);

        progress_tv = (TextView) findViewById(R.id.progress_tv);
        exercise_title = (TextView) findViewById(R.id.exercise_title);
        times_tv = (TextView) findViewById(R.id.times_tv);
        time_tv = (TextView) findViewById(R.id.time_tv);
        next_exercise_tv = (TextView) findViewById(R.id.next_exercise);
        next_exercise_sign_tv = (TextView) findViewById(R.id.next_exercise_sign);

        cont_button = (Button) findViewById(R.id.cont_btn);

        time_pb = (ProgressBar) findViewById(R.id.time_bar);

        action_gif = (GifImageView) findViewById(R.id.action_gif);

        tick_player = MediaPlayer.create(this, R.raw.tick);
        end_player = MediaPlayer.create(this, R.raw.end);
        victory_player = MediaPlayer.create(this, R.raw.victory);

        Intent intent = getIntent();
        training_id = intent.getStringExtra("training_id");
        exercises = SportDbHelper.getInstance(this).getTrainingExercises(Integer.parseInt(training_id));
        exercise_index = 0;
        isRest = true;

        cont_button.setOnClickListener(this);
        cont_button.callOnClick();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            start_date = LocalDateTime.now();
        }

        start = System.currentTimeMillis();
    }

    private void hideView(View v) {
        v.setFocusable(false);
        v.setVisibility(View.INVISIBLE);
    }

    private void showView(View v) {
        v.setFocusable(true);
        v.setVisibility(View.VISIBLE);
    }


    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onClick(View v) {
        if (exercise_timer != null)
            exercise_timer.cancel();

        if (exercise_index == exercises.size()) {

            finish = System.currentTimeMillis();

            victory_player.start();

            saveDoneTraining();

            Intent intent = new Intent(ExercisesPipelineActivity.this, TrainingOverActivity.class);

            intent.putExtra("total_time", (finish - start) / 1000);
            intent.putExtra("training_id", training_id);
            startActivity(intent);

            return;
        }

        if (isRest) {
            hideView(times_tv);

            showView(next_exercise_sign_tv);
            showView(next_exercise_tv);
            showView(time_tv);
            showView(time_pb);

            progress_tv.setText(String.format("%d/%d", exercise_index + 1, exercises.size()));
            exercise_title.setText(getResources().getString(R.string.rest));
            next_exercise_tv.setText(getString(getResources().getIdentifier(exercises.get(exercise_index).title,
                    "string", getPackageName())));

            action_gif.setImageResource(getResources().getIdentifier(exercises.get(exercise_index).title,
                    "drawable", getPackageName()));

            startTimer(REST_TIME);
            isRest = false;
        } else {
            hideView(next_exercise_sign_tv);
            hideView(next_exercise_tv);
            hideView(time_tv);
            hideView(time_pb);

            exercise_title.setText(getResources().getIdentifier(exercises.get(exercise_index).title,
                    "string", getPackageName()));

            String desc = exercises.get(exercise_index).description;
            if (desc.contains("s")) {
                showView(time_tv);
                showView(time_pb);
                startTimer(Long.parseLong(desc.substring(0, desc.length() - 1)));
            } else {
                showView(times_tv);
                times_tv.setText(desc);
            }

            isRest = true;
            exercise_index++;
        }

    }

    private void saveDoneTraining() {
        int duration = (int) ((finish - start) / 1000),
                tr_id = Integer.parseInt(training_id);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                SportDbHelper.getInstance(this).insertDoneTraining(tr_id, duration, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(start_date));
            }
        } catch (SportDbHelper.DbException ex)
        {
            Toast.makeText(this, getString(R.string.save_training_fail), Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer(long time_in_seconds) {
        time_pb.setMax((int) time_in_seconds);
        time_pb.setProgress(0);

        exercise_timer = new CountDownTimer(time_in_seconds * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long seconds_left = millisUntilFinished / 1000;
                time_tv.setText(Long.toString(seconds_left));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    time_pb.incrementProgressBy(1);
                }
                tick_player.start();
            }

            @Override
            public void onFinish() {
                end_player.start();
                cont_button.callOnClick();
            }
        };
        exercise_timer.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (exercise_timer != null)
            exercise_timer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        onBackPressed();
    }

}
