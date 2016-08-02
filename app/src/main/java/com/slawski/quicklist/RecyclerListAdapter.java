package com.slawski.quicklist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Adapter for our recycler view to display tasks.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    // Private variable that stores all the tasks to be displayed.
    private List<Task> tasks = new ArrayList<>();
    private final HashMap<Integer, Integer> originalVotes = new HashMap<>();

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
        for(Task task : tasks) {
            this.originalVotes.put(task.getID(), task.getVotes());
        }
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
        this.originalVotes.remove(position);
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
        holder.voteCountTextView.setText(String.valueOf(tasks.get(position).getVotes()));
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
        this.originalVotes.clear();
        for(Task task : tasks) {
            this.originalVotes.put(task.getID(), task.getVotes());
        }
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;
        public ImageButton upvote;
        public ImageButton downvote;
        public TextView voteCountTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.list_task_description);

            // Hook up the click events for the voting buttons
            upvote = (ImageButton) itemView.findViewById(R.id.upArrow);
            downvote = (ImageButton) itemView.findViewById(R.id.downArrow);
            voteCountTextView = (TextView) itemView.findViewById(R.id.voteCounter);

            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task task = tasks.get(getAdapterPosition());
                    updateTask(view.getContext(), new Task(task.getID(), task.getTaskDescription(),
                            task.getVotes()+1));

                }
            });

            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Task task = tasks.get(getAdapterPosition());
                    updateTask(view.getContext(), new Task(task.getID(), task.getTaskDescription(),
                            task.getVotes()-1));
                }
            });
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        private void updateTask(Context context, Task task) {
            DatabaseHandler db = new DatabaseHandler(context);
            db.updateTask(task);
            voteCountTextView.setText(String.valueOf(task.getVotes()));
            tasks.set(getAdapterPosition(), task);

            if(task.getVotes() > originalVotes.get(task.getID())) {
                upvote.getDrawable().setColorFilter(Color.parseColor("#FF8B60"), PorterDuff.Mode.MULTIPLY);
                downvote.getDrawable().setColorFilter(null);
            } else if(task.getVotes() < originalVotes.get(task.getID())) {
                downvote.getDrawable().setColorFilter(Color.parseColor("#9494ff"), PorterDuff.Mode.MULTIPLY);
                upvote.getDrawable().setColorFilter(null);
            } else {
                downvote.getDrawable().setColorFilter(null);
                upvote.getDrawable().setColorFilter(null);
            }
            notifyItemChanged(getAdapterPosition());
        }
    }
}
