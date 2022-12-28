package com.nikitavenediktov.sportapp;

public class Exercise {
    public int id;
    public String title, gif, description;

    public Exercise(int id, String title, String gif, String description)
    {
        this.id = id;
        this.title = title;
        this.gif = gif;
        this.description = description;
    }
}
