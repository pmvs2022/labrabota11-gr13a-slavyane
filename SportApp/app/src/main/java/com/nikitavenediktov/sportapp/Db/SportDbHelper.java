package com.nikitavenediktov.sportapp.Db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nikitavenediktov.sportapp.Models.DoneTraining;
import com.nikitavenediktov.sportapp.Models.Exercise;
import com.nikitavenediktov.sportapp.Models.Training;

import java.util.ArrayList;

public class SportDbHelper extends SQLiteOpenHelper {

    //Singleton pattern
    private static SportDbHelper helperInstance;

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
    public static final String COLUMN_FK_TYPE = "Type_id";
    public static final String COLUMN_COMPLEXITY = "Complexity";

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

    public static synchronized SportDbHelper getInstance(Context context)
    {
        if (helperInstance == null)
            helperInstance = new SportDbHelper(context.getApplicationContext());

        return helperInstance;
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
            insertExercise(db, "sit_ups", "12");
            insertExercise(db, "elbow_plank", "20s");
            insertExercise(db, "horizontal_scissors",  "16");
            insertExercise(db, "climber", "14");

            //for press N2
            insertExercise(db, "sit_ups", "20");
            insertExercise(db, "legs_lifts", "15");
            insertExercise(db, "side_twisting", "20");
            insertExercise(db, "elbow_plank", "50s");
            insertExercise(db, "side_plank", "35s");
            insertExercise(db, "legs_swinging", "45s");

            //for legs N1
            insertExercise(db, "glute_bridge_steps", "20");
            insertExercise(db, "dumbbell_swinging", "10");
            insertExercise(db, "reverse_lunges", "14");
            insertExercise(db, "squats",  "30");

            //for legs N2
            insertExercise(db, "pistol_squats", "15");
            insertExercise(db, "side_lunges", "30");
            insertExercise(db, "calf_raising", "30");

            //for back N1
            insertExercise(db, "pull_ups", "10");
            insertExercise(db, "renegade_row", "12");
            insertExercise(db, "dumbbell_pullover", "12");
            insertExercise(db, "dumbbell_upright_row", "12");

            //press N1
            insertTraining(db, "easy", "press", new String[][]{
                    {"sit_ups", "12"},
                    {"elbow_plank", "20s"},
                    {"horizontal_scissors", "16"},
                    {"climber", "14"}
            });

            //press N2
            insertTraining(db, "hard", "press", new String[][]{
                    {"sit_ups", "20"},
                    {"legs_lifts", "15"},
                    {"side_twisting", "20"},
                    {"elbow_plank", "50s"},
                    {"side_plank", "35s"},
                    {"legs_swinging", "45s"}
            });

            //legs N1
            insertTraining(db, "medium", "legs", new String[][]{
                    {"glute_bridge_steps", "20"},
                    {"dumbbell_swinging", "10"},
                    {"reverse_lunges", "14"},
                    {"squats",  "30"}
            });


            //legs N2
            insertTraining(db, "hard", "legs", new String[][]{
                    {"squats",  "30"},
                    {"pistol_squats", "15"},
                    {"side_lunges", "30"},
                    {"calf_raising", "30"},
                    {"squats",  "30"}
            });

            //back N1
            insertTraining(db, "medium", "back", new String[][]{
                    {"pull_ups", "10"},
                    {"dumbbell_swinging", "10"},
                    {"renegade_row", "12"},
                    {"dumbbell_pullover", "12"},
                    {"dumbbell_upright_row", "12"}
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
                COLUMN_DESC + " TEXT);";

        db.execSQL(query);
    }

    private void createTrainingTable(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_TRAINING_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_FK_TYPE + " INTEGER NOT NULL," +
                COLUMN_COMPLEXITY + " TEXT NOT NULL," +
                "CHECK (" + COLUMN_COMPLEXITY + " IN ('easy', 'medium', 'hard'))," +
                "FOREIGN KEY(" + COLUMN_FK_TYPE + ") REFERENCES " + TABLE_TYPE_NAME
                + "(" + COLUMN_ID + "));";

        db.execSQL(query);
    }

    private void createTrainingExerciseTable(SQLiteDatabase db)
    {
        String query = "CREATE TABLE " + TABLE_TRAINING_EXERCISE_NAME + "(" +
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

    private void insertExercise(SQLiteDatabase db, String title, String description)
            throws DbException
    {
        ContentValues cv = new ContentValues();
        long res;

        cv.put(COLUMN_TITLE, title);
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
    private void insertTraining(SQLiteDatabase db, String complexity, String type, String[][] exercises_title_desc)
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
        cv.put(COLUMN_FK_TYPE, type_id);
        cv.put(COLUMN_COMPLEXITY, complexity);
        training_id = db.insert(TABLE_TRAINING_NAME, null, cv);
        if (training_id == -1)
            throw new DbException(TABLE_TRAINING_NAME + " INSERT: " + type);
        else
            System.out.println("SUCCESS");

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
                throw new DbException(TABLE_TRAINING_EXERCISE_NAME + " INSERT: " + exercises_title_desc[i][0]);
        }

        cursor.close();
    }

    public ArrayList<Training> getTypedTrainings(String type)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int id_index, type_id_index, complexity_index;
        ArrayList<Training> trainings = new ArrayList<>();

        String query = String.format("SELECT %s.* FROM %s INNER JOIN %s on %s.%s = %s.%s WHERE %s.%s=?;"
                , TABLE_TRAINING_NAME, TABLE_TRAINING_NAME, TABLE_TYPE_NAME, TABLE_TRAINING_NAME,
                COLUMN_FK_TYPE, TABLE_TYPE_NAME, COLUMN_ID, TABLE_TYPE_NAME, COLUMN_TITLE);

        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(query, new String[]{type});

        if (cursor == null || !cursor.moveToFirst())
            return trainings;

        id_index = cursor.getColumnIndex(COLUMN_ID);
        type_id_index = cursor.getColumnIndex(COLUMN_FK_TYPE);
        complexity_index = cursor.getColumnIndex(COLUMN_COMPLEXITY);

        do {
            trainings.add(new Training(cursor.getInt(id_index), cursor.getInt(type_id_index),
                    type, cursor.getString(complexity_index)));
        } while(cursor.moveToNext());

        return trainings;
    }

    public ArrayList<String> getTypes()
    {
        int title_index;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> types = new ArrayList<>();
        Resources resources = context.getResources();

        Cursor cursor = db.query(TABLE_TYPE_NAME, new String[]{COLUMN_TITLE}, null,
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst())
        {
            title_index = cursor.getColumnIndex(COLUMN_TITLE);

            do {
                String temp_type = cursor.getString(title_index);
                types.add(temp_type);
            } while(cursor.moveToNext());

            cursor.close();
        }

      //  db.close();
        return types;
    }

    public ArrayList<Exercise> getTrainingExercises(int training_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Exercise> exercises = new ArrayList<>();
        int id_index, title_index, desc_index;
        String query = String.format("SELECT %s.* FROM %s INNER JOIN %s ON %s.%s = %s.%s " +
                "INNER JOIN %s ON %s.%s = %s.%s WHERE %s.%s=?;", TABLE_EXERCISE_NAME, TABLE_EXERCISE_NAME,
                TABLE_TRAINING_EXERCISE_NAME, TABLE_EXERCISE_NAME, COLUMN_ID, TABLE_TRAINING_EXERCISE_NAME,
                COLUMN_FK_EXERCISE, TABLE_TRAINING_NAME, TABLE_TRAINING_EXERCISE_NAME, COLUMN_FK_TRAINING,
                TABLE_TRAINING_NAME, COLUMN_ID, TABLE_TRAINING_NAME, COLUMN_ID);

        Cursor cursor = db.rawQuery(query, new String[] {Integer.toString(training_id)});

        if (cursor == null || !cursor.moveToFirst())
            return exercises;

        id_index = cursor.getColumnIndex(COLUMN_ID);
        title_index = cursor.getColumnIndex(COLUMN_TITLE);
        desc_index = cursor.getColumnIndex(COLUMN_DESC);

        do {
            int id = cursor.getInt(id_index);
            String title = cursor.getString(title_index),
            desc = cursor.getString(desc_index);

            exercises.add(new Exercise(id, title, desc));

        } while(cursor.moveToNext());

        return exercises;
    }

    public ArrayList<DoneTraining> getDoneTrainings(String start_date) // yyyy-MM-dd
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DoneTraining> doneTrainings = new ArrayList<>();
        int id_index, training_id_index, duration_index, start_date_index, type_title_index,
                complexity_index;
        String query =
                String.format("SELECT %s.*, %s.%s, %s.%s FROM %s INNER JOIN %s ON %s.%s = %s.%s " +
                        "INNER JOIN %s ON %s.%s = %s.%s", TABLE_DONE_TRAINING_NAME, TABLE_TYPE_NAME,
                        COLUMN_TITLE, TABLE_TRAINING_NAME, COLUMN_COMPLEXITY, TABLE_DONE_TRAINING_NAME,
                        TABLE_TRAINING_NAME, TABLE_DONE_TRAINING_NAME, COLUMN_FK_TRAINING,
                        TABLE_TRAINING_NAME, COLUMN_ID, TABLE_TYPE_NAME, TABLE_TRAINING_NAME,
                        COLUMN_FK_TYPE, TABLE_TYPE_NAME, COLUMN_ID);

        query += (start_date == null) ? ";" : String.format(" WHERE date(%s.%s)=?;",
                TABLE_DONE_TRAINING_NAME, COLUMN_START_DATE);

        Cursor cursor = (start_date == null) ? db.rawQuery(query, null) :
                db.rawQuery(query, new String[]{start_date});

        if (cursor == null || !cursor.moveToFirst())
            return doneTrainings;

        id_index = cursor.getColumnIndex(COLUMN_ID);
        training_id_index = cursor.getColumnIndex(COLUMN_FK_TRAINING);
        duration_index = cursor.getColumnIndex(COLUMN_DURATION);
        start_date_index = cursor.getColumnIndex(COLUMN_START_DATE);
        type_title_index = cursor.getColumnIndex(COLUMN_TITLE);
        complexity_index = cursor.getColumnIndex(COLUMN_COMPLEXITY);

        do {
            int id = cursor.getInt(id_index),
                    training_id = cursor.getInt(training_id_index),
                    duration = cursor.getInt(duration_index);
            start_date = cursor.getString(start_date_index);
            String type_title = cursor.getString(type_title_index),
                    complexity = cursor.getString(complexity_index);

            doneTrainings.add(new DoneTraining(id, training_id, duration, start_date, type_title, complexity));

        } while(cursor.moveToNext());

        return doneTrainings;
    }


    public class DbException extends  Exception
    {
        public DbException(String message)
        {
            super(message);
        }
    }
}
