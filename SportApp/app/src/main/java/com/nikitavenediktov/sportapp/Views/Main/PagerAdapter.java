package com.nikitavenediktov.sportapp.Views.Main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.Fragment;

import com.nikitavenediktov.sportapp.R;
import com.nikitavenediktov.sportapp.Views.Main.CalendarFragment;
import com.nikitavenediktov.sportapp.Views.Main.TrainingListFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[] {R.string.trainings, R.string.calendar, R.string.sports_facts};

    private final Context mContext;

    public PagerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        Bundle args;

        switch(position)
        {
            case 0:
                fragment = new TrainingListFragment();
                break;
            case 1:
                fragment = new CalendarFragment();
                break;
            case 2:
                fragment = new FactsFragment();
                break;
        }

        args = new Bundle();
        args.putInt(null, position + 1);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() { return TAB_TITLES.length; }

}

