package com.slawski.quicklist;

import android.content.ContentProvider;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    // Reference to our List View
    //TODO make private
    ListView taskListView;
    MyBaseAdapter adapter;
    SimpleCursorAdapter adapter2;
    private static final int LOADER_ID = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        DatabaseHandler db = new DatabaseHandler(this);

        // Insert some sample tasks
        db.addTask(new Task("Buy milk", 1));
        db.addTask(new Task("Take out trash", 4));
        db.addTask(new Task("Walk dog", 10));

        Log.d("Reading: ", "Reading all contacts...");
        List<Task> testTasks = db.getAllTasks();
        int i = 0;
        for(Task task : testTasks) {
            Log.d("Task: ", "ID: " + task.getID() + " description: " + task.getTaskDescription()
                    + " votes: " + task.getVotes());

            // Add task to our array list which we will use to create ListView
        }

        taskListView = (ListView) findViewById(R.id.lvTasks);
        taskListView.setNestedScrollingEnabled(true);

        //Get initial tasks from db
//        DatabaseHandler db = new DatabaseHandler(this);
//        List<Task> dbTasks = db.getAllTasks();
//        adapter = new MyBaseAdapter(ScrollingActivity.this, dbTasks);
//        taskListView.setAdapter(adapter);

        adapter2 = new SimpleCursorAdapter(this, R.layout.task_list_item, null,
                new String[] { DatabaseHandler.KEY_TASK_DESCRIPTION },
                new int[] {R.id.list_task_description}, 0);
        taskListView.setAdapter(adapter2);
        getLoaderManager().initLoader(LOADER_ID, null, this);

        // Generated Code
        // ***************************************************************
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        DatabaseHandler db = new DatabaseHandler(this);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Collections.sort(tasks, new Comparator<Task>() {
//                    @Override
//                    public int compare(Task task1, Task task2) {
//                        return task1._taskDescription.compareTo(task2._taskDescription);
//                    }
//                });
//                MyBaseAdapter baseAdapter = new MyBaseAdapter(ScrollingActivity.this, tasks);
//                taskListView.setAdapter(baseAdapter);
//                baseAdapter.notifyDataSetChanged();
//            }
//        });
        // *************************************************************************

        FloatingActionButton addTaskButton = (FloatingActionButton) findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScrollingActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                getLoaderManager().restartLoader(LOADER_ID, null, this);
            }
        }
    }

    // Generated Code
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    // Generated Code
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id != LOADER_ID) {
            return null;
        }
        return new CursorLoader(ScrollingActivity.this, TaskContentProvider.CONTENT_URI,
                new String[] {DatabaseHandler.KEY_TASK_DESCRIPTION },
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter2.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter2.swapCursor(null);
    }
}
