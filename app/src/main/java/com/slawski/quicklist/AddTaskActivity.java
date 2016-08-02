package com.slawski.quicklist;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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

    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
        } else if(id == R.id.action_add_task) {
            DatabaseHandler db = new DatabaseHandler(this);
            //TODO 1) add error handling (i.e. empty task)
            //TODO 2) add support for the due date for the database
            //TODO 3) send back the result of add task so it can be appended to the adapter
            //TODO 4) figure out a better way to create ids
            EditText taskDescription = (EditText) findViewById(R.id.taskDescriptionField);
            Intent i = new Intent();
            i.putExtra("task", taskDescription.getText().toString());
            setResult(RESULT_OK, i);
            finish();
        }

        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_task, menu);
        return true;
    }

    private void updateDueDateTextBox() {
        EditText dateInput = (EditText) findViewById(R.id.datePickerTest);
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateInput.setText(sdf.format(myCalendar.getTime()));
    }
}
