package com.slawski.quicklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.slawski.quicklist.Database.DatabaseHelper;
import com.slawski.quicklist.Models.Task;
import com.slawski.quicklist.Models.TaskWrapper;
import com.slawski.quicklist.Recycler.ItemTouchCallbackHelper;
import com.slawski.quicklist.Recycler.RecyclerListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity for the application
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Reference to the adapter required by the RecyclerViewer.
     */
    private RecyclerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Convert all Tasks to TaskWrappers
        List<TaskWrapper> taskWrappers = new ArrayList<>();
        for(Task task : getAllTasks()) {
            taskWrappers.add(new TaskWrapper(task));
        }

        // Setup the RecyclerView that will display all the tasks.
        FrameLayout background = (FrameLayout) findViewById(R.id.swipe_bg);
        adapter = new RecyclerListAdapter(this, taskWrappers, background);
        adapter.sortTasks();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        ItemTouchHelper.Callback callback = new ItemTouchCallbackHelper(adapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        //TODO: Dividers in RecyclerView that allow swiping animate pretty funny. Disabling for now
        //RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(this);
        //recyclerView.addItemDecoration(dividerItemDecoration);

        // Setup the toolbar and floating action button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton addTaskButton = (FloatingActionButton) findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    /**
     * Called when the application returns to the MainActivity after being on the 'add task' screen.
     * @param requestCode request code
     * @param resultCode result code to indicate the outcome of the request
     * @param data data from the request
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                // Pass along the task description to the adapter so it can be added to the RecyclerViewer
                adapter.addTask(data.getExtras().getString("task"));
            }
        }
    }

    /**
     * Inflates the options menu.
     * @param menu menu to inflate
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    /**
     * Called when an options item is selected
     * @param item item selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            adapter.sortTasks();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Queries the database for all tasks.
     */
    public List<Task> getAllTasks() {
        DatabaseHelper db = new DatabaseHelper(this);
        return db.getAllTasks();
    }
}
