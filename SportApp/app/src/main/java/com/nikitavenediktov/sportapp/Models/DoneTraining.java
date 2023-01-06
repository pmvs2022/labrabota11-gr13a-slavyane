package com.nikitavenediktov.sportapp.Models;

import com.github.sundeepk.compactcalendarview.domain.Event;

public class DoneTraining {
    public int id, training_id, duration;
    public String start_date, type, complexity;

    public DoneTraining(int id, int training_id, int duration, String start_date, String type, String complexity)
    {
        this.id = id;
        this.training_id = training_id;
        this.duration = duration;
        this.start_date = start_date;
        this.type = type;
        this.complexity = complexity;
    }
}
