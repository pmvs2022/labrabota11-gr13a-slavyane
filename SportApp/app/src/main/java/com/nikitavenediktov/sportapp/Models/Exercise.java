package com.nikitavenediktov.sportapp.Models;

public class Exercise {
    public int id;
    public String title, description;

    public Exercise(int id, String title, String description)
    {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
