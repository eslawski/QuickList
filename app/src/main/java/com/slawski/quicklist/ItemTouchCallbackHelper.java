package com.slawski.quicklist;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class ItemTouchCallbackHelper extends ItemTouchHelper.Callback {

    private final RecyclerListAdapter mAdapter;
    FrameLayout frameLayout;

    /**
     * Constructor
     * @param adapter
     */
    public ItemTouchCallbackHelper(
            RecyclerListAdapter adapter, FrameLayout frameLayout) {
        mAdapter = adapter;
        this.frameLayout = frameLayout;
    }

    /**
     * Helps specify which types of swipes and drags are accepted.
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;
    }

    /**
     * Add the "leave behind" effect
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        frameLayout.setY(itemView.getTop());
        frameLayout.setX(0);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(itemView.getWidth(), itemView.getHeight()));
        if(isCurrentlyActive) {
            frameLayout.setVisibility(View.VISIBLE);
        }else{
            // This will immediately hide the background as soon as you let go. A litte more stable
            // but doesn't look as cool.
            //frameLayout.setVisibility(View.GONE);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Give the background a second to dismiss. Not quite the best way to do this. Once or twice
        // I have seen an issue with the item dividers overlapping the background. An easy way might
        // be to remove the borders. Otherwise this animation placement needs to be reworked.
        int duration = 1000;
        if(viewHolder.getAdapterPosition() == mAdapter.getItemCount()-1) {
            // Shorter duration for the last item in the list
            duration = 100;
        }
        TranslateAnimation animate = new TranslateAnimation(0,0,0,0);
        animate.setDuration(duration);
        frameLayout.startAnimation(animate);
        frameLayout.setVisibility(View.GONE);
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    /**
     * Indicates of the 'long press' is enabled to initiate drag events and such. Going to
     * just disable this for now.
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * Indicates if item swiping will be enabled.
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {
        // We only want the active item
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                ItemTouchHelperViewHolder itemViewHolder =
                        (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }
    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            ItemTouchHelperViewHolder itemViewHolder =
                    (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }
}
