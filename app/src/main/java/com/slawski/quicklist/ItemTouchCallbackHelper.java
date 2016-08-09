package com.slawski.quicklist;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * Class that captures various UI interactions with the RecyclerView and its views.
 * See the callbacks provided in the ItemTouchHelperAdapter interface (implemented by the adapter)
 * which are invoked to notify the adapter that some action needs to be taken.
 */
public class ItemTouchCallbackHelper extends ItemTouchHelper.Callback {

    /**
     * Reference to the RecyclerListAdapter that coordinates all the views in the RecyclerList.
     */
    private final RecyclerListAdapter mAdapter;

    /**
     * True if the user is touching a view while performing a swipe. False otherwise.
     */
    private boolean swipeInProgress;

    /**
     * Constructor
     * @param adapter The RecyclerListAdapter that is responsible for coordinating all the views
     */
    public ItemTouchCallbackHelper(RecyclerListAdapter adapter) {
        mAdapter = adapter;
    }

    /**
     * Specifies which directions of movement should be enabled. Enable both drag and swipe even
     * though I am only really concerned with swipe. If drag is ever needed see the onMove method.
     * @param recyclerView The Recycler view containing all the views
     * @param viewHolder The view holder
     * @return The movement flags
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * Called when a view is dragged from its original position. This method simply calls the onItemMoved
     * method on the adapter in case the adapter wants to do something special when an item is dragged.
     * @param recyclerView The RecyclerView
     * @param oldPosition The view holder for the old position
     * @param newPosition The target view holder the new position
     * @return boolean
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder oldPosition, RecyclerView.ViewHolder newPosition) {
        mAdapter.onItemMove(oldPosition.getAdapterPosition(),
                newPosition.getAdapterPosition());
        return true;
    }

    /**
     * Called when a view is being drawn. After some investigation, this function is called repeatedly
     * as views are being swiped by the user. This becomes especially useful for displaying the 'swipe
     * background' as the user drags views across the screen.
     * @param c Canvas
     * @param recyclerView The RecyclerView
     * @param viewHolder The view holder of the view being drawn
     * @param dX Horizontal coordinate of the item being drawn
     * @param dY Vertical coordinate of the item being drawn
     * @param actionState Action state of the view being drawn
     * @param isCurrentlyActive Indicates if the view is currently being touched/drawn.
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        if(isCurrentlyActive) {
            swipeInProgress = true;
            mAdapter.onSwipeInProgress(itemView.getTop(), itemView.getWidth(), itemView.getHeight());
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    /**
     * Called when an item is swiped off of the screen. Notify the adapter that the view has been
     * swiped away so that it can delete the task from the list/database.
     * @param viewHolder The view holder swiped away
     * @param direction The direction of the swipe
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Item has been swiped away. No longer in progress.
        swipeInProgress = false;
        mAdapter.onSwipeCompleted(viewHolder.getAdapterPosition());
    }

    /**
     * Indicates of the 'long press' is enabled to initiate drag events and such. Going to
     * just disable this for now in case I want it later.
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * Indicates if item swiping will be enabled.
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * Called when the view holder being swiped/dragged is changed. This is useful for visual indication
     * of which view holder is currently active (or last active).
     * @param viewHolder The selected view holder
     * @param actionState action state of the view
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * Called when the animation for swiping away or dropping a currently swiped view
     * has been completed.
     * @param recyclerView The RecyclerView
     * @param viewHolder The view holder containing the view that was being swiped
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        // If a swipe was still in progress at this point we know that the user canceled their swipe
        if(swipeInProgress){
            mAdapter.onSwipeCanceled();
        }
        swipeInProgress = false;
    }
}
