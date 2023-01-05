package com.nikitavenediktov.sportapp;

public class Training {
    int id, type_id;
    String type, complexity;

    public Training(int id, int type_id, String type, String complexity)
    {
        this.id = id;
        this.type_id = type_id;
        this.type = type;
        this.complexity = complexity;
    }
}
