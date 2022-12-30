package com.nikitavenediktov.sportapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TrainingOverActivity extends AppCompatActivity implements View.OnClickListener{
    private String training_id;
    private long total_time;

    Button share_btn;
    TextView total_time_tv;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_over);

        share_btn = findViewById(R.id.share_btn);
        total_time_tv = findViewById(R.id.total_time_tv);

        Intent intent = getIntent();

        training_id = intent.getStringExtra("training_id");
        total_time = intent.getLongExtra("total_time", 0);

        share_btn.setOnClickListener(this);
        total_time_tv.setText(String.format("%s %d %s %d %s", total_time_tv.getText(),
                total_time / 60, getString(R.string.min), total_time % 60, getString(R.string.sec)));
    }


    @Override
    public void onClick(View v) {
        // share logic
    }
}
