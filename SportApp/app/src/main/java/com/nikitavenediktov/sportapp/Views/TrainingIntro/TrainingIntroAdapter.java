package com.nikitavenediktov.sportapp.Views.TrainingIntro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nikitavenediktov.sportapp.Models.Exercise;
import com.nikitavenediktov.sportapp.R;

import java.util.ArrayList;

public class TrainingIntroAdapter extends RecyclerView.Adapter<TrainingIntroAdapter.MyViewHolder>{

    private Context context;
    private String training_id;
    private ArrayList<Exercise> exercises;

    public TrainingIntroAdapter(Context context, String training_id, ArrayList<Exercise> exercises)
    {
        this.context = context;
        this.training_id = training_id;
        this.exercises = exercises;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.exercise_row, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.ex_title.setText(context.getResources().getIdentifier(exercises.get(position).title,
                "string", context.getPackageName()));

        String desc = exercises.get(position).description;
        if (desc.contains("s"))
            holder.ex_desc.setText(desc.substring(0, desc.length() - 1) + " " +
                    context.getResources().getString(R.string.sec));
        else
            holder.ex_desc.setText(desc);
    }

    @Override
    public int getItemCount() { return exercises.size(); }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ex_title, ex_desc;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            ex_title = (TextView) itemView.findViewById(R.id.ex_title);
            ex_desc = (TextView) itemView.findViewById(R.id.ex_desc);
        }
    }

}
