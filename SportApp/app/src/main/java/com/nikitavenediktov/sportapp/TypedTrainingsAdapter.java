package com.nikitavenediktov.sportapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Layout;
import android.util.Pair;
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

    private ArrayList<Pair<Integer, String>> trainings_pair;
    private ArrayList<String> trainings;

    public TypedTrainingsAdapter(Context context, ArrayList<Pair<Integer, String>> trainings_pair)
    {
        this.context = context;
        this.trainings_pair = trainings_pair;

        trainings = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            trainings_pair.forEach(pair -> trainings.add(context.getResources().getString(R.string.training) + " #" + pair.first));
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
        holder.complexity_txt.setText(context.getString(context.getResources()
                .getIdentifier(trainings_pair.get(position).second, "string", context.getPackageName())));
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

        intent.putExtra("training_id", Integer.toString(trainings_pair.get(index).first));
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
