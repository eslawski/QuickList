package com.slawski.quicklist;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Adapter for our recycler view to display tasksWrappers.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    // Private variable that stores all the tasksWrappers to be displayed.
    private List<TaskWrapper> tasksWrappers = new ArrayList<>();

    // Stores the context for database purposes. Not the best solution here because the adapter
    // should not be manipulating the data.
    private Context context;

    /**
     * Constructor
     */
    RecyclerListAdapter(Context context, List<TaskWrapper> taskWrappers) {
        this.tasksWrappers = taskWrappers;
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
        //TODO somehow figure out how to delete tasksWrappers from the actual database.
        DatabaseHandler db = new DatabaseHandler(context);
        db.deleteTask(tasksWrappers.get(position).getTask());
        tasksWrappers.remove(position);
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
        TaskWrapper taskWrapper = tasksWrappers.get(position);
        Task task = taskWrapper.getTask();

        holder.textView.setText(task.getTaskDescription());
        holder.voteCountTextView.setText(String.valueOf(task.getVotes()));

        Drawable upvoteDrawable = holder.upvote.getDrawable().mutate();
        Drawable downvoteDrawable = holder.downvote.getDrawable().mutate();

        if(taskWrapper.getIsUpvoted()) {
            upvoteDrawable.setColorFilter(Color.parseColor("#FF8B60"), PorterDuff.Mode.MULTIPLY);
            downvoteDrawable.setColorFilter(null);
        } else if(taskWrapper.getIsDownvoted()) {
            downvoteDrawable.setColorFilter(Color.parseColor("#9494ff"), PorterDuff.Mode.MULTIPLY);
            upvoteDrawable.setColorFilter(null);
        } else {
            upvoteDrawable.setColorFilter(null);
            downvoteDrawable.setColorFilter(null);
        }

        holder.upvote.setImageDrawable(upvoteDrawable);
        holder.downvote.setImageDrawable(downvoteDrawable);

    }

    public void sortTasks() {
        Collections.sort(tasksWrappers, new Comparator<TaskWrapper>() {
            public int compare(TaskWrapper taskWrapper1, TaskWrapper taskWrapper2) {
                return taskWrapper2.getTask().getVotes() - taskWrapper1.getTask().getVotes();
            }
        });
        notifyDataSetChanged();
    }

    /**
     * Method from RecyclerView.Adapter
     * @return
     */
    @Override
    public int getItemCount() {
        return tasksWrappers.size();
    }

    public void addTask(String taskDescription) {
        DatabaseHandler db = new DatabaseHandler(context);
        int nextRecordId = db.getRecordId()+1;
        Task newTask = new Task(nextRecordId, taskDescription, 0);
        db.addTask(newTask);
        this.tasksWrappers.add(new TaskWrapper(newTask));
        notifyItemInserted(this.tasksWrappers.size()-1);
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
                    TaskWrapper taskWrapper = tasksWrappers.get(getAdapterPosition());
                    taskWrapper.upVote();
                    updateTask(view.getContext(), taskWrapper);

                }
            });

            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TaskWrapper taskWrapper = tasksWrappers.get(getAdapterPosition());
                    // Do not allow down-voting below 0
                    if(taskWrapper.getTask().getVotes() > 0) {
                        taskWrapper.downVote();
                        updateTask(view.getContext(), taskWrapper);
                    }

                }
            });
        }

        @Override
        public void onItemSelected() {
            // Used for indicating which row is selected
            //itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            // Used for indicating the last attempted cleared item
            //itemView.setBackgroundColor(0);
        }

        private void updateTask(Context context, TaskWrapper taskWrapper) {
            DatabaseHandler db = new DatabaseHandler(context);
            Task task = taskWrapper.getTask();
            db.updateTask(task);
            voteCountTextView.setText(String.valueOf(task.getVotes()));
            tasksWrappers.set(getAdapterPosition(), taskWrapper);
            notifyItemChanged(getAdapterPosition());
        }
    }
}
