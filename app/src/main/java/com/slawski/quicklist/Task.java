package com.slawski.quicklist;

/**
 * Class that defines was a Task consist of.
 */
public class Task {

    /**
     * Id of the task.
     */
    int _id;

    /**
     * Description of the task.
     */
    String _taskDescription;

    /**
     * Number of votes for the task.
     */
    int _votes;

    /**
     * Empty Constructor
     */
    public Task() {

    }

    /**
     * Constructor for a new task with an id.
     * @param id id of the task
     * @param taskDescription description of the task
     * @param votes number of votes for the task
     */
    public Task(int id, String taskDescription, int votes) {
        this._id = id;
        this._taskDescription = taskDescription;
        this._votes = votes;
    }

    /**
     * Constructor for a new task without an id
     * @param taskDescription description of the task
     * @param votes number of votes
     */
    public Task(String taskDescription, int votes) {
        this._taskDescription = taskDescription;
        this._votes = votes;
    }

    /**
     * Gets the Id of the task
     * @return id of the task
     */
    public int getID() {
        return this._id;
    }

    /**
     * Sets the Id of the task
     * @param id id for the task
     */
    public void setID(int id) {
        this._id = id;
    }

    /**
     * Gets the description of the task
     * @return description of the task
     */
    public String getTaskDescription() {
        return this._taskDescription;
    }

    /**
     * Sets the description of the task
     * @param taskDescription description for the task
     */
    public void setTaskDescription(String taskDescription) {
        this._taskDescription = taskDescription;
    }

    /**
     * Gets the number of votes for the task.
     * @return number of votes for the task
     */
    public int getVotes() {
        return this._votes;
    }

    /**
     * Sets the number of votes for the task.
     * @param votes number of votes for the task
     */
    public void setVotes(int votes) {
        this._votes = votes;
    }

}
