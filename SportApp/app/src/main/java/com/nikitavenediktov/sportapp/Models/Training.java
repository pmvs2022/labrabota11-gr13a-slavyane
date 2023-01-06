package com.nikitavenediktov.sportapp.Models;

public class Training {
    public int id, type_id;
    public String type, complexity;

    public Training(int id, int type_id, String type, String complexity)
    {
        this.id = id;
        this.type_id = type_id;
        this.type = type;
        this.complexity = complexity;
    }
}
