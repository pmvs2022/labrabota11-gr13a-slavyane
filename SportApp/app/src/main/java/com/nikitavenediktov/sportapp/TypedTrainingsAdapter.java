package com.nikitavenediktov.sportapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TypedTrainingsAdapter extends RecyclerView.Adapter<TypedTrainingsAdapter.MyViewHolder> implements View.OnClickListener
{

    private Context context;

    private ArrayList<Integer> training_ids;
    private ArrayList<String> trainings;

    public TypedTrainingsAdapter(Context context, ArrayList<Integer> training_ids)
    {
        this.context = context;
        this.training_ids = training_ids;

        trainings = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            training_ids.forEach(id -> trainings.add(context.getResources().getString(R.string.training) + " #" + id));
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.training_row, parent, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.training_txt.setText(trainings.get(position));
    }

    @Override
    public int getItemCount()
    {
        return trainings.size();
    }

    @Override
    public void onClick(final View v) {
        int index = ((RecyclerView)((Activity) context).
                findViewById(R.id.typed_trainings)).getChildLayoutPosition(v);

        Intent intent = new Intent(context, TrainingIntroActivity.class);

        intent.putExtra("training_id", Integer.toString(training_ids.get(index)));
        ((Activity) context).startActivity(intent);

        //   Toast.makeText(context, trainings.get(index), Toast.LENGTH_SHORT)
        //         .show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView training_txt;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            training_txt = (TextView) itemView.findViewById(R.id.training_num);
        }

    }
}
