package com.nikitavenediktov.sportapp;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class CalendarFragment extends Fragment
{
    CalendarView calendar;

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
    }

}
