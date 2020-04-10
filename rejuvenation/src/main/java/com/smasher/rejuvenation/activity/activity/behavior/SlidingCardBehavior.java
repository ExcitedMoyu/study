package com.smasher.rejuvenation.activity.activity.behavior;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

/**
 * @author matao
 * @date 2019/6/4
 */
public class SlidingCardBehavior extends CoordinatorLayout.Behavior<SlidingCardLayout> {


    private static final String TAG = "SlidingCardBehavior";

    /**
     * 默认偏移量
     */
    private int mInitialOffset;

    @Override
    public boolean onMeasureChild(@NonNull CoordinatorLayout parent, @NonNull SlidingCardLayout child,
                                  int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        int offset = getChildMeasureOffset(parent, child);
        Log.d(TAG, "onMeasureChild:  offset=" + offset);
        int height = View.MeasureSpec.getSize(parentHeightMeasureSpec) - heightUsed - offset;
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        child.measure(parentWidthMeasureSpec, heightMeasureSpec);
        return true;
    }

    /**
     * 获取卡片默认偏移量
     *
     * @param parent parent
     * @param child  child
     * @return
     */
    private int getChildMeasureOffset(CoordinatorLayout parent, SlidingCardLayout child) {
        int offset = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            //排出自己
            if (view != child && view instanceof SlidingCardLayout) {
                int header = ((SlidingCardLayout) view).getHeaderViewHeight();
                offset += header;
            }
        }
        return offset;
    }


    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull SlidingCardLayout child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        SlidingCardLayout previous = getPreviousChild(parent, child);
        if (previous != null) {
            int offset = previous.getTop() + previous.getHeaderViewHeight();
            child.offsetTopAndBottom(offset);
        } else {
            Log.d(TAG, "onLayoutChild: Layout  previous is null");
        }
        mInitialOffset = child.getTop();
        return true;
    }

    /**
     * 上一个卡片
     *
     * @param parent parent
     * @param child  child
     * @return
     */
    private SlidingCardLayout getPreviousChild(CoordinatorLayout parent, SlidingCardLayout child) {
        int cardIndex = parent.indexOfChild(child);
        for (int i = cardIndex - 1; i >= 0; i--) {
            View view = parent.getChildAt(i);
            if (view instanceof SlidingCardLayout) {
                return (SlidingCardLayout) view;
            }
        }
        return null;
    }


    /**
     * 下一个卡片
     *
     * @param parent parent
     * @param child  child
     * @return
     */
    private SlidingCardLayout getNextChild(CoordinatorLayout parent, SlidingCardLayout child) {
        int cardIndex = parent.indexOfChild(child);
        for (int i = cardIndex + 1; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view instanceof SlidingCardLayout) {
                return (SlidingCardLayout) view;
            }
        }
        return null;
    }


    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SlidingCardLayout child,
                                  @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (child.getTop() > mInitialOffset) {
            //1、控制自己的滑动
            int minOffset = mInitialOffset;
            int maxOffset = mInitialOffset + child.getHeight() - child.getHeaderViewHeight();
            consumed[1] = scroll(child, dy, minOffset, maxOffset);
            //2、控制上面和下面的滑动
            shiftScroll(consumed[1], coordinatorLayout, child);
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SlidingCardLayout child,
                                       @NonNull View directTargetChild, @NonNull View target, int axes) {
        Log.d(TAG, "onStartNestedScroll: " + child.getId());
        boolean isVertical = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        return isVertical && child == directTargetChild;
    }




    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SlidingCardLayout child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //1、控制自己的滑动
        int minOffset = mInitialOffset;
        int maxOffset = mInitialOffset + child.getHeight() - child.getHeaderViewHeight();
        int scrollY = scroll(child, dyUnconsumed, minOffset, maxOffset);
        //2、控制上面和下面的滑动
        shiftScroll(scrollY, coordinatorLayout, child);
    }

    private void shiftScroll(int scrollY, CoordinatorLayout parent, SlidingCardLayout child) {
        if (scrollY == 0) {
            return;
        }
        if (scrollY > 0) {
            //往上推
            SlidingCardLayout current = child;
            SlidingCardLayout card = getPreviousChild(parent, current);
            while (card != null) {
                int delta = calcOtherOffset(card, current);
                if (delta > 0) {
                    card.offsetTopAndBottom(-delta);
                }
                current = card;
                card = getPreviousChild(parent, current);
            }
        } else {//往下推
            SlidingCardLayout current = child;
            SlidingCardLayout card = getNextChild(parent, current);
            while (card != null) {
                int delta = calcOtherOffset(current, card);
                if (delta > 0) {
                    card.offsetTopAndBottom(delta);
                }
                current = card;
                card = getNextChild(parent, current);
            }

        }
    }



    /**
     * 计算其它卡片的偏移值
     *
     * @param above above
     * @param below below
     * @return
     */
    private int calcOtherOffset(SlidingCardLayout above, SlidingCardLayout below) {
        return above.getTop() + above.getHeaderViewHeight() - below.getTop();
    }



    private int scroll(SlidingCardLayout child, int dy, int minOffset, int maxOffset) {
        int initialOffset = child.getTop();
        int offset = clamp(initialOffset - dy, minOffset, maxOffset) - initialOffset;
        child.offsetTopAndBottom(offset);
        return -offset;
    }


    private int clamp(int i, int minOffset, int maxOffset) {
        if (i > maxOffset) {
            return maxOffset;
        } else if (i < minOffset) {
            return minOffset;
        } else {
            return i;
        }

    }
}
