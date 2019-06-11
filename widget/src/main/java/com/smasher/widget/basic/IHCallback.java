package com.smasher.widget.basic;

import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author matao
 * @date 2019/6/10
 */
public class IHCallback extends ItemTouchHelper.Callback {


    private final int mDefaultScrollX;
    private int mCurrentScrollX;
    private int mCurrentScrollXWhenInactive;
    private float mInitXWhenInactive;
    private boolean mFirstInactive;


    private static final String TAG = "IHCallback";
    private MoveAdapter mMoveAdapter;


    public IHCallback(MoveAdapter moveAdapter, int defaultScrollX) {
        mMoveAdapter = moveAdapter;
        mDefaultScrollX = defaultScrollX;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //允许上下的拖动
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //只允许从右向左侧滑
        int swipeFlags = ItemTouchHelper.LEFT;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

        if (mMoveAdapter != null) {
            mMoveAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }

        return true;
    }


    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        Log.d(TAG, "onMoved: ");
    }

    @Override
    public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return super.getMoveThreshold(viewHolder);
    }


    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mMoveAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }


    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        Log.d(TAG, "getSwipeEscapeVelocity: " + defaultValue);
        return super.getSwipeEscapeVelocity(defaultValue);
        //return Integer.MAX_VALUE;
    }


    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return super.getSwipeThreshold(viewHolder);
        //return Integer.MAX_VALUE;
    }


    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return super.getSwipeVelocityThreshold(defaultValue);
    }


    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (viewHolder != null) {
            int position = viewHolder.getAdapterPosition();

            switch (actionState) {
                case ItemTouchHelper.ACTION_STATE_IDLE:
                    Log.d(TAG, "onSelectedChanged: " + position + "  actionState:ACTION_STATE_IDLE = " + actionState);
                    break;
                case ItemTouchHelper.ACTION_STATE_SWIPE:
                    Log.d(TAG, "onSelectedChanged: " + position + "  actionState:ACTION_STATE_SWIPE = " + actionState);
                    break;
                case ItemTouchHelper.ACTION_STATE_DRAG:
                    Log.d(TAG, "onSelectedChanged: " + position + "  actionState:ACTION_STATE_DRAG = " + actionState);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        // 首次滑动时，记录下ItemView当前滑动的距离
//        if (dX == 0) {
//            mCurrentScrollX = viewHolder.itemView.getScrollX();
//            mFirstInactive = true;
//        }
//        if (isCurrentlyActive) {
//            // 手指滑动
//            // 基于当前的距离滑动
//            viewHolder.itemView.scrollTo(mCurrentScrollX + (int) -dX, 0);
//        } else { // 动画滑动
//            if (mFirstInactive) {
//                mFirstInactive = false;
//                mCurrentScrollXWhenInactive = viewHolder.itemView.getScrollX();
//                mInitXWhenInactive = dX;
//            }
//            if (viewHolder.itemView.getScrollX() >= mDefaultScrollX) {
//                // 当手指松开时，ItemView的滑动距离大于给定阈值，那么最终就停留在阈值，显示删除按钮。
//                viewHolder.itemView.scrollTo(Math.max(mCurrentScrollX + (int) -dX, mDefaultScrollX), 0);
//            } else {
//                // 这里只能做距离的比例缩放，因为回到最初位置必须得从当前位置开始，dx不一定与ItemView的滑动距离相等
//                viewHolder.itemView.scrollTo((int) (mCurrentScrollXWhenInactive * dX / mInitXWhenInactive), 0);
//            }
//        }
    }


    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }


    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    @Override
    public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder current, @NonNull RecyclerView.ViewHolder target) {
        return super.canDropOver(recyclerView, current, target);
    }


    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }


    @Override
    public int getBoundingBoxMargin() {
        return super.getBoundingBoxMargin();
    }


    @Override
    public int interpolateOutOfBoundsScroll(@NonNull RecyclerView recyclerView, int viewSize, int viewSizeOutOfBounds, int totalSize, long msSinceStartScroll) {
        return super.interpolateOutOfBoundsScroll(recyclerView, viewSize, viewSizeOutOfBounds, totalSize, msSinceStartScroll);
    }

    @Override
    public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder.itemView.getScrollX() > mDefaultScrollX) {
            viewHolder.itemView.scrollTo(mDefaultScrollX, 0);
        } else if (viewHolder.itemView.getScrollX() < 0) {
            viewHolder.itemView.scrollTo(0, 0);
        }
        mMoveAdapter.onSaveItemStatus(viewHolder);
//        mItemTouchStatus.onSaveItemStatus(viewHolder);
//        mItemTouchStatus.onSaveItemStatus(viewHolder);
    }

    @Override
    public RecyclerView.ViewHolder chooseDropTarget(@NonNull RecyclerView.ViewHolder selected, @NonNull List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {
        return super.chooseDropTarget(selected, dropTargets, curX, curY);
    }
}


