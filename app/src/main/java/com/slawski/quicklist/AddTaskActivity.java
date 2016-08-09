package com.slawski.quicklist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
//EVAn COMP

/**
 * Activity for adding a new task.
 */
public class AddTaskActivity extends AppCompatActivity {

    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        /**
         * Setup the calendar that will be used for the due date field.
         */
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateDueDateTextBox();
            }
        };

        EditText dateInput = (EditText) findViewById(R.id.datePickerTest);

        // Listen on focus change for the date field because listening on the click for some reason
        // requires two clicks events.
        dateInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            //TODO probably also want to do this on click as well
            public void onFocusChange(View v, boolean hasFocus) {
                // Need to use the focus change handler because on click requires two clicks
                if(hasFocus) {
                    new DatePickerDialog(AddTaskActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        // Setup the task description input field.
        EditText taskDescription =(EditText) findViewById(R.id.taskDescriptionField);
        if(taskDescription.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

    }

    /**
     * Called when the back button or "ADD TASK" action item is selected. If the back buton was
     * selected finish the activity without sending result back. If the "ADD TASK" button is
     * selected sent the task description back.
     * @param item item
     */
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
        } else if(id == R.id.action_add_task) {
            EditText taskDescription = (EditText) findViewById(R.id.taskDescriptionField);
            Intent i = new Intent();
            i.putExtra("task", taskDescription.getText().toString());
            setResult(RESULT_OK, i);
            finish();
        }

        return true;
    }

    /**
     * Create the options menu.
     * @param menu menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_task, menu);
        return true;
    }

    /**
     * Date selection handler that updates the due date text box with the date selected
     * by the calendar view.
     */
    private void updateDueDateTextBox() {
        EditText dateInput = (EditText) findViewById(R.id.datePickerTest);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateInput.setText(sdf.format(myCalendar.getTime()));
    }
}
