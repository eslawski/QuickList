package com.slawski.quicklist;

/**
 * Interface that allows us to plug into certain user interactions controlled by ItemTouchCallbackHelper.
 * This allows the adapter to perform any logic it needs as views are interacted with. (i.e. swiping tasks)
 *
 * A similar pattern can be followed for the ViewHolder to control the state specific views as
 * they are interacted with. (i.e styling a task to appear 'selected').
 */
public interface ItemTouchHelperAdapter {

    /**
     * Callback for when an item is dragged.
     * @param fromPosition from position
     * @param toPosition to position
     */
    void onItemMove(int fromPosition, int toPosition);

    /**
     * Callback for when an swipe is completed.
     * @param position Position for which the swipe was completed
     */
    void onSwipeCompleted(int position);

    /**
     * Callback for when a swipe is in progress and the 'swipe background' needs to be displayed.
     * The swipe background will be displayed with the exact dimensions/position of the view holder
     * creating a nice "background" effect as the swipe is in progress.
     * @param top Vertical coordinate of the view holder for the view being swiped
     * @param width Width of the view holder for the view being swiped
     * @param height Height of the view holder for the view being swiped
     */
    void onSwipeInProgress(int top, int width, int height);

    /**
     * Callback for when a swipe is canceled by the user.
     */
    void onSwipeCanceled();
}
