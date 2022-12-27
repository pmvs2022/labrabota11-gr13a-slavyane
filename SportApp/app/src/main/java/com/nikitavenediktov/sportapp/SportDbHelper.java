package com.nikitavenediktov.sportapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SportDbHelper extends SQLiteOpenHelper {

    // Database
    public static final String DATABASE_NAME = "Sport.db";
    public static final int DATABASE_VERSION = 1;

    private Context context;

    //Shared columns
    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_TITLE = "Title";

    // Table type
    public static final String TABLE_TYPE_NAME = "Type";

    // Table Exercise
    public static final String TABLE_EXERCISE_NAME = "Exercise";
    public static final String COLUMN_GIF = "Gif";
    public static final String COLUMN_DESC = "Description";

    //Table Training
    public static final String TABLE_TRAINING_NAME = "Training";
    public static final String COLUMN_FK_Type = "Type_id";

    //Table Training & Exercise
    public static final String TABLE_TRAINING_EXERCISE_NAME = "Training_Exercise";
    public static final String COLUMN_FK_TRAINING = "Training_id";
    public static final String COLUMN_FK_EXERCISE = "Exercise_id";

    //Table Done training
    public static final String TABLE_DONE_TRAINING_NAME = "DoneTraining";
    public static final String COLUMN_START_DATE = "StartDate";
    public static final String COLUMN_DURATION = "Duration";

    public SportDbHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.beginTransaction();

            //DDL
            createTypeTable(db);
            createExerciseTable(db);
            createTrainingTable(db);
            createTrainingExerciseTable(db);
            createDoneTrainingTable(db);

            //DML
            insertType(db, "legs");
            insertType(db, "back");
            insertType(db, "press");
            insertType(db, "biceps");
            insertType(db, "general");

            //for press N1
            insertExercise(db, "twisting", "twisting.gif", "12");
            insertExercise(db, "elbow_plank", "elbow_plank.gif", "20s");
            insertExercise(db, "horizontal_scissors", "horizontal_scissors.gif", "16");
            insertExercise(db, "climber", "climber.gif", "14");

            //for press N2
            insertExercise(db, "twisting", "twisting.gif", "20");
            insertExercise(db, "legs_lift", "legs_lift.gif", "15");
            insertExercise(db, "side_twisting", "side_twisting.gif", "20");
            insertExercise(db, "elbow_plank", "elbow_plank.gif", "50s");
            insertExercise(db, "side_plank", "side_plank.gif", "35s");
            insertExercise(db, "legs_swinging", "legs_swinging.gif", "45s");

            //for legs N1
            insertExercise(db, "glute_bridge_steps", "glute_bridge_steps.gif", "20");
            insertExercise(db, "dumbbell_swinging", "dumbbell_swinging.gif", "10");
            insertExercise(db, "reverse_lunges", "reverse_lunges.gif", "14");
            insertExercise(db, "situps", "reverse_lunges.gif", "30");

            //press N1
            insertTraining(db, "press", new String[][]{
                    {"twisting", "12"},
                    {"elbow_plank", "20s"},
                    {"horizontal_scissors", "16"},
                    {"climber", "14"}
            });

            db.setTransactionSuccessful();
            db.endTransaction();

        }
        catch (DbException ex)
        {
            System.err.println(ex.getMessage());
        }


    }

    private void createTypeTable(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_TYPE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT NOT NULL);";

        db.execSQL(query);
    }

    private void createExerciseTable(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_EXERCISE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT NOT NULL," +
                COLUMN_GIF + " TEXT NOT NULL," +
                COLUMN_DESC + " TEXT CHECK(" + COLUMN_DESC + " LIKE '[0-9]+s?'));";

        db.execSQL(query);
    }

    private void createTrainingTable(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_TRAINING_EXERCISE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_FK_Type + " INTEGER NOT NULL," +
                "FOREIGN KEY(" + COLUMN_FK_Type + ") REFERENCES " + TABLE_TYPE_NAME
                + "(" + COLUMN_ID + "));";

        db.execSQL(query);
    }

    private void createTrainingExerciseTable(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_TRAINING_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_FK_TRAINING + " INTEGER NOT NULL," +
                COLUMN_FK_EXERCISE + " INTEGER NOT NULL," +
                "FOREIGN KEY(" + COLUMN_FK_TRAINING + ") REFERENCES " + TABLE_TRAINING_NAME +
                "(" + COLUMN_ID + ")," +
                "FOREIGN KEY(" + COLUMN_FK_EXERCISE + ") REFERENCES " + TABLE_EXERCISE_NAME +
                "(" + COLUMN_ID + "));";

        db.execSQL(query);
    }

    private void createDoneTrainingTable(SQLiteDatabase db)
    {
        // YYYY-MM-DD HH:MM:SS.SSS - datetime format
        String query = "CREATE TABLE " + TABLE_DONE_TRAINING_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_START_DATE + " TEXT NOT NULL," +
                COLUMN_DURATION + " INTEGER NOT NULL," +
                COLUMN_FK_TRAINING + " INTEGER NOT NULL," +
                "FOREIGN KEY(" + COLUMN_FK_TRAINING + ") REFERENCES " + TABLE_TRAINING_NAME +
                "(" + COLUMN_ID + "));";

        db.execSQL(query);
    }


    private void insertType(SQLiteDatabase db, String title) throws DbException
    {
        ContentValues cv = new ContentValues();
        long res;

        cv.put(COLUMN_TITLE, title);

        res = db.insert(TABLE_TYPE_NAME, null, cv);
        if (res == -1)
            throw new DbException(TABLE_TYPE_NAME + " INSERT: " + title);
    }

    private void insertExercise(SQLiteDatabase db, String title, String gif, String description)
            throws DbException
    {
        ContentValues cv = new ContentValues();
        long res;

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_GIF, gif);
        cv.put(COLUMN_DESC, description);

        res = db.insert(TABLE_EXERCISE_NAME, null, cv);
        if (res == -1)
            throw new DbException(TABLE_EXERCISE_NAME + " INSERT: " + title);
    }

    public void insertDoneTraining(int training_id, int duration, String start_date)
            throws DbException {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        long res;

        cv.put(COLUMN_START_DATE, start_date);
        cv.put(COLUMN_DURATION, duration);
        cv.put(COLUMN_FK_TRAINING, training_id);

        res = db.insert(TABLE_DONE_TRAINING_NAME, null , cv);
        if (res == -1)
            throw new DbException(TABLE_DONE_TRAINING_NAME + " INSERT: " + training_id);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db, TABLE_DONE_TRAINING_NAME);
        dropTable(db, TABLE_TRAINING_EXERCISE_NAME);
        dropTable(db, TABLE_TRAINING_NAME);
        dropTable(db, TABLE_EXERCISE_NAME);
        dropTable(db, TABLE_TYPE_NAME);

        onCreate(db);
    }

    private void dropTable(SQLiteDatabase db, String table)
    {
        db.execSQL("DROP TABLE IF EXISTS " + table);
    }
    private void insertTraining(SQLiteDatabase db, String type, String[][] exercises_title_desc)
            throws DbException {
        int type_id, id_index;
        long training_id;

        ContentValues cv = new ContentValues();

        // Getting type id
        Cursor cursor = db.query(TABLE_TYPE_NAME, new String[]{COLUMN_ID}, COLUMN_TITLE + "=?",
                new String[] {type}, null, null, null);

        if (cursor == null || !cursor.moveToFirst())
            throw new DbException(TABLE_TYPE_NAME + " QUERY: " + type);

        id_index = cursor.getColumnIndex(COLUMN_ID);
        if (id_index == -1)
            throw new DbException(TABLE_TYPE_NAME + " QUERY: " + type);

        type_id = cursor.getInt(id_index);

        cursor.close();

        //insert training
        cv.put(COLUMN_FK_Type, type_id);
        training_id = db.insert(TABLE_TRAINING_NAME, null, cv);
        if (training_id == -1)
            throw new DbException(TABLE_TRAINING_NAME + "INSERT: " + type);

        //insert training-exercise dependencies
        for(int i = 0; i < exercises_title_desc.length; ++i)
        {
            // Getting exercise id
            cursor = db.query(TABLE_EXERCISE_NAME, new String[]{COLUMN_ID},
                    COLUMN_TITLE + "=? AND " + COLUMN_DESC + "=?",
                    new String[]{exercises_title_desc[i][0], exercises_title_desc[i][1]},
                    null, null, null);

            if (cursor == null || !cursor.moveToFirst())
                throw new DbException(TABLE_EXERCISE_NAME + " QUERY: " + type);

            id_index = cursor.getColumnIndex(COLUMN_ID);
            if (id_index == -1)
                throw new DbException(TABLE_EXERCISE_NAME + " QUERY: " + type);

            int exercise_id = cursor.getInt(id_index);

            //formation training-exercise dependency
            cv = new ContentValues();
            cv.put(COLUMN_FK_TRAINING, training_id);
            cv.put(COLUMN_FK_EXERCISE, exercise_id);

            //insert training-exercise dependency
            long pair_id = db.insert(TABLE_TRAINING_EXERCISE_NAME, null, cv);
            if (pair_id == -1)
                throw new DbException(TABLE_TRAINING_EXERCISE_NAME + "INSERT: " + exercises_title_desc[i][0]);
        }

        cursor.close();
    }
    public class DbException extends  Exception
    {
        public DbException(String message)
        {
            super(message);
        }
    }
}
