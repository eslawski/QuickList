package com.slawski.quicklist;
//EVAN COMP

/**
 * This class wraps a task along with a 'deltaVote' integer to provide an in memory object that the
 * RecyclerView can use to display. The 'deltaVote' variable tracks all upvotes/downvotes since
 * the last time the application was opened (or when a new task is added). This allows for custom
 * styling for the upvote/downvote buttons.
 */
public class TaskWrapper {

    /**
     * The task.
     */
    Task task;

    /**
     * The delta votes of the task since the application was last opened. Minimum vote is 0.
     */
    int deltaVote;

    /**
     * Constructor of a TaskWrapper. Default deltaVotes is 0.
     * @param task task to wrap
     */
    public TaskWrapper(Task task) {
        this.task = task;
        this.deltaVote = 0;
    }

    /**
     * Upvotes the task.
     */
    public void upVote() {
        deltaVote++;
        task.setVotes(task.getVotes() + 1);
    }

    /**
     * Downvotes the task.
     */
    public void downVote() {
        deltaVote--;
        task.setVotes(task.getVotes() - 1);
    }

    /**
     * Obtains the Task from the TaskWrapper
     * @return the task wrapped by this class
     */
    public Task getTask() {
       return task;
    }

    /**
     * @return Returns true if the wrapped task has a net upvote since application has been opened.
     */
    public boolean getIsUpvoted() {
        return deltaVote > 0;
    }

    /**
     * @return Returns true if the wrapped task has a net downvote since application has been opened.
     */
    public boolean getIsDownvoted() {
        return deltaVote < 0;
    }

}
