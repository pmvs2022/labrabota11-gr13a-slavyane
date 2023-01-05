package com.nikitavenediktov.sportapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DayDoneTrainingsAdapter extends RecyclerView.Adapter<DayDoneTrainingsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<DoneTraining> doneTrainings;
    public DayDoneTrainingsAdapter(Context context, ArrayList<DoneTraining> doneTrainings)
    {
        this.context = context;
        this.doneTrainings = doneTrainings;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.done_training_row, parent, false);

        return new DayDoneTrainingsAdapter.MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.training_tv.setText(String.format("%d. %s #%d", position + 1, context.getResources().getString(R.string.training),
                doneTrainings.get(position).training_id));

        holder.duration_tv.setText(String.format("%s %d %s %d %s",
                context.getResources().getString(R.string.duration), doneTrainings.get(position).duration
                 / 60, context.getString(R.string.min), doneTrainings.get(position).duration % 60,
                context.getString(R.string.sec)));

        holder.complexity_tv.setText(context.getResources().getString(context.getResources()
                .getIdentifier(doneTrainings.get(position).complexity, "string", context.getPackageName())));

        holder.type_tv.setText(context.getResources().getString(context.getResources()
                .getIdentifier(doneTrainings.get(position).type, "string", context.getPackageName())));

        try {
            holder.start_time_tv.setText(String.format("%s %s", context.getResources().getString(R.string.start_time),
                    DayDoneTrainingsActivity.DATE_FORMAT_TIME.format(CalendarFragment.DATE_FORMAT_DONE_TRAINING.parse(doneTrainings.get(position).start_date))));
        }
        catch (ParseException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return doneTrainings.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView training_tv, duration_tv, start_time_tv, complexity_tv, type_tv;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            training_tv = (TextView) itemView.findViewById(R.id.training_tv);
            duration_tv = (TextView) itemView.findViewById(R.id.duration_tv);
            start_time_tv = (TextView) itemView.findViewById(R.id.start_time_tv);
            complexity_tv = (TextView) itemView.findViewById(R.id.complexity_tv);
            type_tv = (TextView) itemView.findViewById(R.id.type_tv);
        }
    }
}
