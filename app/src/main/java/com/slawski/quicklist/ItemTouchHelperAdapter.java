package com.slawski.quicklist;

/**
 * Interface that allows us to pass onMove and onSwiped event callbacks up the chain.
 */
public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void displaySwipeBackground(int top, int width, int height);
}
