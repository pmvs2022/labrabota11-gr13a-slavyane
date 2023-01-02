package com.nikitavenediktov.sportapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

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
        @SuppressLint("UseCompatLoadingForDrawables")
        BitmapDrawable drawable = (BitmapDrawable) getDrawable(R.drawable.sport);
        Bitmap bitmap = drawable.getBitmap();
        String message = getShareMessage();

        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,
                "logo", null);

        Uri uri = Uri.parse(bitmapPath);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, getString(R.string.share_via)));
    }

    @SuppressLint("DefaultLocale")
    private String getShareMessage()
    {
        ArrayList<Exercise> exercises = SportDbHelper.getInstance(this)
                .getTrainingExercises(Integer.parseInt(training_id));

        StringBuilder message = new StringBuilder(getString(R.string.share_msg_start));
        for(Exercise exercise : exercises)
        {
            message.append("\n - ").append(getString(getResources().getIdentifier(exercise.title, "string", getPackageName())));
            message.append(" (");
            if (exercise.description.contains("s"))
                message.append(exercise.description.subSequence(0, exercise.description.length() - 1))
                        .append(' ').append(getString(R.string.sec)).append(')');
            else
                message.append(exercise.description).append(')');
        }
        message.append('\n').append(getString(R.string.share_msg_time)).append(String.format(" %d %s %d %s.",
                total_time / 60, getString(R.string.min), total_time % 60, getString(R.string.sec)));
        message.append('\n').append(getString(R.string.share_msg_end)).append(' ').append(getString(R.string.app_name)).append('.');

        return message.toString();
    }
}
