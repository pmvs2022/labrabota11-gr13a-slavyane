package com.nikitavenediktov.sportapp.Views.TypedTrainings;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nikitavenediktov.sportapp.Models.Training;
import com.nikitavenediktov.sportapp.R;
import com.nikitavenediktov.sportapp.Db.SportDbHelper;

import java.util.ArrayList;


public class TypedTrainingsActivity extends AppCompatActivity
{
    private String type_title;
    private ArrayList<Training> trainings;

    RecyclerView recyclerView;

    TypedTrainingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.typed_trainings);

        recyclerView = (RecyclerView) findViewById(R.id.typed_trainings);

        Intent intent = getIntent();
        type_title = intent.getStringExtra("type_title");

        setTitle(getTitle() + ": " + getResources()
                .getString(getResources().getIdentifier(type_title, "string", getPackageName())));

        trainings= SportDbHelper.getInstance(this).getTypedTrainings(type_title);
        adapter = new TypedTrainingsAdapter(TypedTrainingsActivity.this, trainings);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TypedTrainingsActivity.this));
      //  recyclerView.addItemDecoration(new DividerItemDecoration(TypedTrainingsActivity.this, 0));
    }
}

/*public class TypedTrainingsActivity extends ListActivity implements AdapterView.OnItemClickListener {

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
}*/
