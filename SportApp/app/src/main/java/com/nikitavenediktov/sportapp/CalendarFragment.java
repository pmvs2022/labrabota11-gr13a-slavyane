package com.nikitavenediktov.sportapp;

import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarFragment extends Fragment
{
    CompactCalendarView calendar;
    TextView dateTextView;

    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

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
            dateTextView.setText(DateTimeFormatter.ofPattern("MMM yyyy").format(LocalDateTime.now()));
        }

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                dateTextView.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });
        return rootVew;
    }

     /* CalendarView calendar;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.calendar_tab, container, false);

        calendar = (CalendarView)rootView.findViewById(R.id.calendarView);

        calendar.setDate(Calendar.getInstance().getTimeInMillis(),false,true);
        return rootView;
    }*/

}
