package com.slawski.quicklist.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.slawski.quicklist.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Create database helper class that follows the CRUD model. There are currently two tables
 *
 *      1) "tasks" - A table that stores all information relating to a task.
 *      2) "recordId" - A table that stores an auto-incremented integer that acts as the system
 *                      record id to provide all new tasks with a unique id.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Version of the database. Must increment this when schema is changed.
     */
    private static final int DATABASE_VERSION = 8;

    /**
     * Name of the database.
     */
    public static final String DATABASE_NAME = "tasksDB";

    /**
     * Names of the tables within the database.
     */
    public static final String TABLE_TASKS = "tasks";
    public static final String TABLE_RECORD_ID = "recordId";

    /**
     * Names of the columns within the "tasks" table.
     */
    public static final String KEY_ID = "_id";
    public static final String KEY_TASK_DESCRIPTION = "taskDescription";
    public static final String KEY_VOTES = "votes";

    /**
     * Constructor
     * @param context context
     */
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create the table(s) of the database.
     * @param db The database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTasksTable = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TASK_DESCRIPTION
                + " TEXT," + KEY_VOTES + " INTEGER" + ")";

        String createRecordTable = "CREATE TABLE " + TABLE_RECORD_ID + "("
                + KEY_ID + " INTEGER PRIMARY KEY)";

        db.execSQL(createTasksTable);
        db.execSQL(createRecordTable);

        // Insert the first value in the record id table
        ContentValues values = new ContentValues();
        values.put(KEY_ID, 0);
        db.insert(TABLE_RECORD_ID, null, values);
    }

    /**
     * Upgrades the database by dropping all table(s) and recreating them.
     * @param db The database
     * @param oldVersion Old version number of the database
     * @param newVersion New version number of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    /**
     * Gets the latest record id from the "recordId" table. This function is used to help assign
     * a unique integer to each task that gets added.
     * @return The latest record id
     */
    public int getRecordId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT MAX(" + KEY_ID + ") as id FROM " + TABLE_RECORD_ID;
        Cursor cursor = db.rawQuery(query, null);
        int recordId = 0;
        if(cursor != null){
            cursor.moveToFirst();
            recordId = cursor.getInt(0);
            cursor.close();
        }

        db.close();
        return recordId;
    }

    /**
     * Adds a new task to the "tasks" table of the database.
     * @param task The task to add.
     */
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create the ContentValues that will be passed to the insert method.
        ContentValues values = new ContentValues();
        values.put(KEY_ID, task.getID());
        values.put(KEY_TASK_DESCRIPTION, task.getTaskDescription());
        values.put(KEY_VOTES, task.getVotes());

        db.insert(TABLE_TASKS, null, values);

        // Increment the record id of the "recordId" table
        ContentValues values1 = new ContentValues();
        values1.put(KEY_ID, task.getID()+1);
        db.update(TABLE_RECORD_ID, values1, null, null);

        db.close();
    }

    /**
     * Returns the task with the specified id.
     * @param id The id of the task
     */
    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, new String[] {KEY_TASK_DESCRIPTION},
                KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);

        Task task = null;
        if(cursor != null) {
            cursor.moveToFirst();
            task = new Task(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), Integer.parseInt(cursor.getString(2)));
            cursor.close();
        }
        db.close();
        return task;
    }

    /**
     * Obtains all the tasks from the database.
     */
    public List<Task> getAllTasks(){
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop over all the records and add them to the taskList.
        if(cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setID(Integer.parseInt(cursor.getString(0)));
                task.setTaskDescription(cursor.getString(1));
                task.setVotes(Integer.parseInt(cursor.getString(2)));
                taskList.add(task);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taskList;
    }

    /**
     * Returns the total number of tasks stored in the database.
     */
    public int getTasksCount() {
        String countQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        db.close();
        return count;
    }

    /**
     * Update the provided task in the database. Returns the id of the updated task.
     * @param task The task to update
     */
    public int updateTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, task.getID());
        values.put(KEY_TASK_DESCRIPTION, task.getTaskDescription());
        values.put(KEY_VOTES, task.getVotes());

        return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[] {String.valueOf(task.getID())});
    }

    /**
     * Deletes the provided task from the database.
     * @param task The task to delete.
     */
    public void deleteTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?", new String[] {String.valueOf(task.getID())});
        db.close();
    }
}
