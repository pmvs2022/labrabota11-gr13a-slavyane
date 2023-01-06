package com.nikitavenediktov.sportapp.Views.Main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.fragment.app.ListFragment;

import com.nikitavenediktov.sportapp.Db.SportDbHelper;
import com.nikitavenediktov.sportapp.Views.TypedTrainings.TypedTrainingsActivity;

import java.util.ArrayList;

public class TrainingListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private ArrayList<String> types;

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
            types.forEach(type -> types_titles.add(getActivity().getResources().getString(getActivity()
                    .getResources().getIdentifier(type, "string", getActivity().getPackageName()))));
        }
        getListView().setOnItemClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, types_titles);
        setListAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(this.getActivity(), TypedTrainingsActivity.class);
        intent.putExtra("type_title", types.get(position));
        startActivity(intent);
    }

}
