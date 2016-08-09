package com.slawski.quicklist.Recycler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.slawski.quicklist.Database.DatabaseHelper;
import com.slawski.quicklist.Models.Task;
import com.slawski.quicklist.Models.TaskWrapper;
import com.slawski.quicklist.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Adapter for the RecyclerView that will display the tasks.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    /**
     * Private variable that stores all the tasksWrappers to be displayed.
     */
    private List<TaskWrapper> tasksWrappers = new ArrayList<>();

    /**
     * Stores the context for database purposes.
     * TODO Not the best solution here because the adapter should not be manipulating the data
     * TODO however there currently no API support for a CursorAdapter with a RecyclerView.
     */
    private Context context;

    /**
     * Reference to a FrameLayout which will be drawn behind the currently swiped task (if any) to
     * create a cool "background" effect while in progress. The background will only be visible if
     * the user is currently touching the task while mid-swipe. If a task is swiped away, it will
     * be animated to stay visible for a short amount of time. Otherwise the background is hidden.
     */
    private FrameLayout background;

    /**
     * Constructor for this class
     * @param context Context
     * @param taskWrappers The list of TaskWrappers to display
     * @param background FrameLayout that will act as the 'swipe background'
     */
    public RecyclerListAdapter(Context context, List<TaskWrapper> taskWrappers, FrameLayout background) {
        this.tasksWrappers = taskWrappers;
        this.context = context;
        this.background = background;
    }

    /**
     * Dragging is disabled for now.
     * @param fromPosition from position
     * @param toPosition to position
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //TODO: Implement dragging if necessary
    }

    /**
     * Invoked when a swipe is completed. Open up a new database connection and delete the task that
     * was swiped while also removing it from the list. Also animate the 'swipe background' such
     * that it stays in view momentarily after the task has been swiped.
     * @param position Position at which the swipe was completed
     */
    @Override
    public void onSwipeCompleted(int position) {
        animateSwipeBackgroundOut(position);
        DatabaseHelper db = new DatabaseHelper(context);
        db.deleteTask(tasksWrappers.get(position).getTask());
        tasksWrappers.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Allows the swipe background display for a short amount of time after a task is swiped.
     * This adds a nice UI effect leaving the background behind as the list re-sizes itself.
     * @param position Position at which the background will be animated out
     */
    public void animateSwipeBackgroundOut(int position) {
        // Give the background a second to dismiss. Not quite the best way to do this.
        int duration = 1000;
        if(position == this.getItemCount()-1) {
            // Shorter duration for the last item in the list
            duration = 400;
        }
        TranslateAnimation animate = new TranslateAnimation(0,0,0,0);
        animate.setDuration(duration);
        background.startAnimation(animate);
        background.setVisibility(View.GONE);
    }

    /**
     * Called when the RecyclerViewer needs a new ViewHolder to represent a new item in the list.
     * Inflate the xml that will be used to draw each task in the list.
     * @param parent ViewGroup
     * @param viewType ViewType
     * @return The new ViewHolder to use
     */
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    /**
     * Called when the RecyclerView needs to display an item at the specified position. This can either
     * be when the item is first create or when it is changed via one of the 'notify*' methods. This
     * method gets called as tasks are upvoted/downvoted and handles all UI related tasks.
     * @param holder ViewHolder to update
     * @param position Position of the ViewHolder
     */
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        TaskWrapper taskWrapper = tasksWrappers.get(position);
        Task task = taskWrapper.getTask();

        holder.textView.setText(task.getTaskDescription());
        holder.voteCountTextView.setText(String.valueOf(task.getVotes()));

        // Android does some weird thing with caching drawables. The workaround for this was
        // to make the drawables 'mutable' so they are not preserved in cache.
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

    /**
     * Sorts the task in the RecyclerView
     */
    public void sortTasks() {
        Collections.sort(tasksWrappers, new Comparator<TaskWrapper>() {
            public int compare(TaskWrapper taskWrapper1, TaskWrapper taskWrapper2) {
                return taskWrapper2.getTask().getVotes() - taskWrapper1.getTask().getVotes();
            }
        });
        notifyDataSetChanged();
    }

    /**
     * Called when a task is actively being swiped (user is touching it). Display the underlying
     * 'swipe background' at the specified position.
     * @param top Position where the top of the swipe background will be displayed
     * @param width Width of the desired swipe background
     * @param height Height of the desired swipe background
     */
    @Override
    public void onSwipeInProgress(int top, int width, int height) {
        background.setY(top);
        background.setX(0);
        background.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        background.setVisibility(View.VISIBLE);
    }

    /**
     * Called if a user cancels their swipe and it returns to it's original position. Hide the 'swipe
     * background'.
     */
    @Override
    public void onSwipeCanceled() {
        background.setVisibility(View.GONE);
    }

    /**
     * Gets the number of items in the RecyclerView. Override required by RecyclerView.Adapter.
     */
    @Override
    public int getItemCount() {
        return tasksWrappers.size();
    }

    /**
     * Creates a new task given the task description and ads it to the database/list. Again
     * this is not the best place to be performing database operations, but it is a workaround to
     * the fact that RecyclerViews do not support CursorAdapters.
     * @param taskDescription
     */
    public void addTask(String taskDescription) {
        DatabaseHelper db = new DatabaseHelper(context);
        int nextRecordId = db.getRecordId()+1;
        Task newTask = new Task(nextRecordId, taskDescription, 0);
        db.addTask(newTask);
        this.tasksWrappers.add(new TaskWrapper(newTask));
        notifyItemInserted(this.tasksWrappers.size()-1);
    }

    /**
     * A RecyclerView consists of a list of 'ViewHolders' each holding one 'View' which represents
     * each row (task). A 'ViewHolder' provides a way to interact with the individual elements of
     * each view that gets created. For example, this is where the upvote/downvote click listeners
     * are implemented.
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ImageButton upvote;
        public ImageButton downvote;
        public TextView voteCountTextView;

        /**
         * Constructor for this class
         * @param itemView View that will be held by this ViewHolder
         */
        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.list_task_description);

            // Hook up the click events for the upvote/downvote buttons
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

        /**
         * Called when a task gets upvoted/downvoted. Open up a database connection and update
         * the task as needed. Again this is not the best place to be performing database
         * operations. Also notify the adapter of the item that changed so it can rebound.
         * @param context context
         * @param taskWrapper The task wrapper
         */
        private void updateTask(Context context, TaskWrapper taskWrapper) {
            DatabaseHelper db = new DatabaseHelper(context);
            Task task = taskWrapper.getTask();
            db.updateTask(task);
            voteCountTextView.setText(String.valueOf(task.getVotes()));
            tasksWrappers.set(getAdapterPosition(), taskWrapper);
            notifyItemChanged(getAdapterPosition());
        }
    }
}
