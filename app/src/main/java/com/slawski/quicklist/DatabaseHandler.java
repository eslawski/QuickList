package com.slawski.quicklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: remove tutorial link
 * Following the tutorial at http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 * Create a database capable of storing user entered tasks following CRUD standards.
 * There were some issues with the tutorial I followed.
 * TODO: avoid opening a new read/writable database for every query.
 */
//TODO could also be called a database helper (see http://javarticles.com/2015/06/android-simplecursoradapter-example.html)
public class DatabaseHandler extends SQLiteOpenHelper {

    // Version of the database. Must increment this when schema is changed.
    private static final int DATABASE_VERSION = 7;

    // Name of the database
    public static final String DATABASE_NAME = "tasksDB";

    // Name of the one and only table (so far) in the database.
    public static final String TABLE_TASKS = "tasks";

    // Column names
    public static final String KEY_ID = "_id";
    public static final String KEY_TASK_DESCRIPTION = "taskDescription";
    public static final String KEY_VOTES = "votes";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create the table(s) of the database.
     * @param db The database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTasksTable = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TASK_DESCRIPTION
                + " TEXT," + KEY_VOTES + " INTEGER" + ")";
        db.execSQL(createTasksTable);
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
     * Adds a new task to the TASKS table of the database.
     * @param task The task to add.
     */
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Create the ContentValues that will be passed to the insert method.
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_DESCRIPTION, task.getTaskDescription());
        values.put(KEY_VOTES, task.getVotes());

        db.insert(TABLE_TASKS, null, values);
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

        if(cursor != null) {
            cursor.moveToFirst();
        }

        //TODO: determine why you need to use the getString method.
        Task task = new Task(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)));

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
        // TODO: close the cursor?

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
     * TODO: determine if this is a useful feature
     * @param task The task to update
     */
    public int updateTask(Task task){
        //TODO: implement
        return 0;
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
