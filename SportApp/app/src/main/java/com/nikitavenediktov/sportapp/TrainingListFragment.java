package com.nikitavenediktov.sportapp;

import android.app.ListActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import java.util.ArrayList;

public class TrainingListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private ArrayList<Pair<String, String>> types;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        SportDbHelper dbHelper = SportDbHelper.getInstance(this.getActivity());
        types = dbHelper.getTypes();
       // dbHelper.close();
        ArrayList<String> types_titles = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            types.forEach(pair -> types_titles.add(pair.second));
        }
        getListView().setOnItemClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, types_titles);
        setListAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(this.getActivity(), TypedTrainingsActivity.class);
        intent.putExtra("type_title", types.get(position).first);
        startActivity(intent);
    }

}
