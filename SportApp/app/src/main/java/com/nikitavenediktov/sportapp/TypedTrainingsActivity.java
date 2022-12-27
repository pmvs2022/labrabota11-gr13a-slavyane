package com.nikitavenediktov.sportapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class TypedTrainingsActivity extends ListActivity implements AdapterView.OnItemClickListener {

    String type_title;
    ArrayList<Integer> training_ids;
    ArrayList<String> trainings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        type_title = intent.getStringExtra("type_title");

        training_ids = SportDbHelper.getInstance(this).getTypedTrainings(type_title);
        trainings = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            training_ids.forEach(id -> trainings.add(getResources().getString(R.string.training) + " №" + id));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, trainings);

        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // go to type page
    }
}
