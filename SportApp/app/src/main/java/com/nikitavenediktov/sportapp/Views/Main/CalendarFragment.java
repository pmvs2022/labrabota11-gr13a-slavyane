package com.nikitavenediktov.sportapp.Views.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.nikitavenediktov.sportapp.Views.DayDoneTrainings.DayDoneTrainingsActivity;
import com.nikitavenediktov.sportapp.Models.DoneTraining;
import com.nikitavenediktov.sportapp.R;
import com.nikitavenediktov.sportapp.Db.SportDbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment
{
    CompactCalendarView calendar;
    TextView dateTextView;

    private ArrayList<DoneTraining> doneTrainings;
    private ArrayList<Event> doneTrainingsEvents;


    public static final SimpleDateFormat DATE_FORMAT_MONTH =
            new SimpleDateFormat("MMM yyyy", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_DONE_TRAINING =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_DAY =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootVew = inflater.inflate(R.layout.custom_calendar_tab, container, false);

        calendar = (CompactCalendarView) rootVew.findViewById(R.id.calendar);
        dateTextView = (TextView) rootVew.findViewById(R.id.date_tv);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTextView.setText(DATE_FORMAT_MONTH.format(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())));
        }

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Intent intent = new Intent(getActivity(), DayDoneTrainingsActivity.class);
                intent.putExtra("date", DATE_FORMAT_DAY.format(dateClicked));

                startActivity(intent);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                dateTextView.setText(DATE_FORMAT_MONTH.format(firstDayOfNewMonth));
            }
        });

        // setting done trainings
        doneTrainings = SportDbHelper.getInstance(rootVew.getContext()).getDoneTrainings(null);
        doneTrainingsEvents = new ArrayList<>();

        for(DoneTraining doneTraining : doneTrainings)
        {
            doneTrainingsEvents.add(DoneTrainingToEvent(doneTraining, rootVew.getContext()));
        }

        calendar.addEvents(doneTrainingsEvents);
        calendar.setUseThreeLetterAbbreviation(true);
        calendar.shouldDrawIndicatorsBelowSelectedDays(true);

        return rootVew;
    }

    private Event DoneTrainingToEvent(DoneTraining doneTraining, Context context)
    {
        try {
            long millis = DATE_FORMAT_DONE_TRAINING.parse(doneTraining.start_date).getTime();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                return new Event(context.getColor(context.getResources()
                        .getIdentifier(doneTraining.type, "color", context.getPackageName())), millis,
                        context.getResources().getString(R.string.training) + " #" + Integer.toString(doneTraining.training_id));
            }
        }
        catch(ParseException ex)
        {
            System.err.println(ex.getMessage());
            return null;
        }

        return null;
        // return new Event(context.getResources().getColor(context.getResources().getIdentifier(doneTraining.)));
    }
}
