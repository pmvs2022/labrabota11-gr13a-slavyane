package com.nikitavenediktov.sportapp.Views.TypedTrainings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nikitavenediktov.sportapp.Models.Training;
import com.nikitavenediktov.sportapp.R;
import com.nikitavenediktov.sportapp.Views.TrainingIntro.TrainingIntroActivity;

import java.util.ArrayList;

public class TypedTrainingsAdapter extends RecyclerView.Adapter<TypedTrainingsAdapter.MyViewHolder> implements View.OnClickListener
{

    private Context context;

    private ArrayList<Training> trainings;
    private ArrayList<String> trainings_titles;

    public TypedTrainingsAdapter(Context context, ArrayList<Training> trainings)
    {
        this.context = context;
        this.trainings = trainings;

        trainings_titles = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            trainings.forEach(training -> trainings_titles.add(context.getResources().getString(R.string.training) + " #" + training.id));
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
        holder.training_txt.setText(trainings_titles.get(position));
        holder.complexity_txt.setText(context.getString(context.getResources()
                .getIdentifier(trainings.get(position).complexity, "string", context.getPackageName())));
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

        intent.putExtra("training_id", Integer.toString(trainings.get(index).id));
        ((Activity) context).startActivity(intent);

        //   Toast.makeText(context, trainings.get(index), Toast.LENGTH_SHORT)
        //         .show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView training_txt, complexity_txt;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            training_txt = (TextView) itemView.findViewById(R.id.training_num);
            complexity_txt = (TextView) itemView.findViewById(R.id.level_tv);
        }

    }
}
