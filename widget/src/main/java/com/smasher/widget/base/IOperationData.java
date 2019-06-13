package com.smasher.widget.base;

/**
 * @author matao
 * @date 2019/6/10
 */
public interface IOperationData {

    /**
     * 数据交换
     *
     * @param fromPosition fromPosition
     * @param toPosition   toPosition
     */
    void onItemMove(int fromPosition, int toPosition);

    /**
     * 数据删除
     *
     * @param position position
     */
    void onItemDismiss(int position);

}
