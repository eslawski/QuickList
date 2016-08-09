package com.slawski.quicklist;


import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * Class that monitors the position of scroll to determine if the floating action button should be
 * hidden. This was implemented to prevent underlying content from being hidden by the action button
 * if the user was scrolled all the way to the bottom. This class gets pluged directly into the
 * floating action button in the xml.
 */
public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    /**
     * Required constructor. Not actually used.
     * @param context context
     * @param attrs attrs
     */
    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
    }

    /**
     * Nested scrolling always enabled.
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    /**
     * This function is called rapidly as the user perform scrolling gestures. Use it to monitor
     * if the user 'over-scrolls' the edge of the content. If so, hide the action button so underlying
     * content is visible. Then show the action button again if the user up scrolls.
     * @param coordinatorLayout CoordinationLayout for which the button belongs
     * @param child child
     * @param target target
     * @param dxConsumed How much horizontal scroll has already been consumed since the start of the gesture.
     * @param dyConsumed How much vertical scroll has already been consumed since the start of the gesture.
     * @param dxUnconsumed How much horizontal scroll has yet to be consumed given the gesture.
     * @param dyUnconsumed How much vertical scroll has yet to be consumed given the gesture.
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
                               FloatingActionButton child, View target, int dxConsumed,
                               int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed);
        // Choosing 50 as an arbitrary number of pixels that seemed like a good amount of 'over-scroll'
        if (child.getVisibility() == View.VISIBLE && dyUnconsumed > 50) {
            child.hide();
        }
    }

    /**
     * Called when the scrolling gesture has concluded.
     * @param coordinatorLayout CoordinationLayout for which the button belongs
     * @param child child
     * @param target target
     */
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        if (child.getVisibility() == View.GONE) {
            child.show();
        }
    }
}
