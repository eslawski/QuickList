package com.slawski.quicklist;


public class TaskWrapper {

    Task task;
    int deltaVote;

    public TaskWrapper(Task task) {
        this.task = task;
        this.deltaVote = 0;
    }

    public void upVote() {
        deltaVote++;
        task.setVotes(task.getVotes() + 1);
    }

    public void downVote() {
        deltaVote--;
        task.setVotes(task.getVotes() - 1);
    }

    public Task getTask() {
       return task;
    }

    public boolean getIsUpvoted() {
        return deltaVote > 0;
    }

    public boolean getIsDownvoted() {
        return deltaVote < 0;
    }

}
