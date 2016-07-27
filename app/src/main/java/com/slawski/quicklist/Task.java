package com.slawski.quicklist;

// Contract class for a Task object that will be stored in the database
public class Task {

    // Private variables
    int _id;
    String _taskDescription;
    int _votes;

    // Empty Constructor
    public Task() {

    }

    // Constructor for new Task object
    public Task(int id, String taskDescription, int votes) {
        this._id = id;
        this._taskDescription = taskDescription;
        this._votes = votes;
    }

    // Constructor for new Task object
    public Task(String taskDescription, int votes) {
        this._taskDescription = taskDescription;
        this._votes = votes;
    }

    // Getter for id variable
    public int getID() {
        return this._id;
    }

    // Setter for id variable
    public void setID(int id) {
        this._id = id;
    }

    // Getter for taskDescription variable
    public String getTaskDescription() {
        return this._taskDescription;
    }

    // Setter for taskDescription variable
    public void setTaskDescription(String taskDescription) {
        this._taskDescription = taskDescription;
    }

    // Getter for votes variable
    public int getVotes() {
        return this._votes;
    }

    // Setter for votes variable
    public void setVotes(int votes) {
        this._votes = votes;
    }

}
