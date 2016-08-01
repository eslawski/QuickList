package com.slawski.quicklist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for our recycler view to display tasks.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    // Private variable that stores all the tasks to be displayed.
    private List<Task> tasks = new ArrayList<>();

    // Stores the context for database purposes. Not the best solution here because the adapter
    // should not be manipulating the data.
    private Context context;

    /**
     * Constructor
     * @param tasks
     */
    RecyclerListAdapter(Context context, List<Task> tasks) {
        this.tasks = tasks;
        this.context = context;
    }

    /**
     * Method from ItemTouchHelperAdapter
     * Dragging is disabled for now.
     * @param fromPosition
     * @param toPosition
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(mItems, i, i + 1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(mItems, i, i - 1);
//            }
//        }
//        notifyItemMoved(fromPosition, toPosition);
//        return true;
    }

    /**
     * Method from ItemTouchHelperAdapter
     * @param position
     */
    @Override
    public void onItemDismiss(int position) {
        //TODO somehow figure out how to delete tasks from the actual database.
        DatabaseHandler db = new DatabaseHandler(context);
        db.deleteTask(tasks.get(position));
        tasks.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Method from RecyclerView.Adapter
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    /**
     * Method from RecyclerView.Adapter
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.textView.setText(tasks.get(position).getTaskDescription());
    }

    /**
     * Method from RecyclerView.Adapter
     * @return
     */
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    /**
     * Refreshes all the tasks in the adapter
     */
    public void refreshAllTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.list_task_description);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
