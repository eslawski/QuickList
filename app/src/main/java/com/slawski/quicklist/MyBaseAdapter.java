package com.slawski.quicklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyBaseAdapter extends BaseAdapter{

    // Stores the tasks we will be working with
    List<Task> tasks = new ArrayList<>();

    // "Inflates" the xml we have defined in task_list_item.xml to actual list items.
    LayoutInflater inflater;

    // Context for which to inflate
    Context context;

    public MyBaseAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TaskViewHolder taskViewHolder;

        if(view == null) {
            view = inflater.inflate(R.layout.task_list_item, parent, false);
            taskViewHolder = new TaskViewHolder(view);
            view.setTag(taskViewHolder);
        } else {
            taskViewHolder = (TaskViewHolder) view.getTag();
        }

        Task task = getItem(position);

        taskViewHolder.description.setText(task.getTaskDescription());

        return view;
    }

    public void add(Task task) {
        tasks.add(task);
        notifyDataSetChanged();
    }

    /**
     * Convenience class for storing the task view. This is helpful because we want to reuse
     * views when possible instead of unconditionally inflating them. This allows for smoother
     * scrolling.
     * TODO: determine how the getTag() function works. Looks like it keeps track of references?
     */
    private class TaskViewHolder {
        TextView description;

        public TaskViewHolder(View task) {
            description = (TextView) task.findViewById(R.id.list_task_description);
        }
    }
}
