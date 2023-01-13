package com.nikitavenediktov.sportapp;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.nikitavenediktov.sportapp.Db.SportDbHelper;
import com.nikitavenediktov.sportapp.Models.Exercise;
import com.nikitavenediktov.sportapp.Models.Training;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class ExerciseResourcesTest {
    private ArrayList<ArrayList<Training>> trainings;
    private SportDbHelper helper;
    private Context context;


    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        helper = SportDbHelper.getInstance(context);
        ArrayList<String> types = helper.getTypes();
        trainings = new ArrayList<ArrayList<Training>>();
        for (int i = 0; i < types.size(); ++i){
            trainings.add(helper.getTypedTrainings(types.get(i)));
        }
    }

    @Test
    public void checkGifs() {
        for (int i = 0; i < trainings.size(); i++){
            for (int j = 0; j < trainings.get(i).size(); ++j){
                ArrayList<Exercise> exercises = helper.getTrainingExercises(trainings.get(i).get(j).id);
                for (int k = 0; k < exercises.size(); ++k){
                    assertNotEquals("No GIF for " + exercises.get(k).title, 0, context.getResources().getIdentifier(exercises.get(k).title, "drawable", context.getPackageName()));
                }
            }
        }
    }

    @Test
    public void checkExerciseNames() {
        for (int i = 0; i < trainings.size(); i++){
            for (int j = 0; j < trainings.get(i).size(); ++j){
                ArrayList<Exercise> exercises = helper.getTrainingExercises(trainings.get(i).get(j).id);
                for (int k = 0; k < exercises.size(); ++k){
                    assertNotEquals("No localized name for " + exercises.get(k).title, 0, context.getResources().getIdentifier(exercises.get(k).title, "string", context.getPackageName()));
                }
            }
        }
    }
}