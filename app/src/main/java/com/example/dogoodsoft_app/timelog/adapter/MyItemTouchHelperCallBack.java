package com.example.dogoodsoft_app.timelog.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by dogoodsoft-app on 2017/11/13.
 */

public class MyItemTouchHelperCallBack extends ItemTouchHelper.Callback{


    private ItemTouchHellperAdapter mAdapter;


    public MyItemTouchHelperCallBack(ItemTouchHellperAdapter adapter) {

        mAdapter = adapter;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; //允许上下的拖动
        //int dragFlags =ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT; //允许左右的拖动
        //int swipeFlags = ItemTouchHelper.LEFT; //只允许从右向左侧滑
        //int swipeFlags = ItemTouchHelper.DOWN; //只允许从上向下侧滑
        //一般使用makeMovementFlags(int,int)或makeFlag(int, int)来构造我们的返回值
        //makeMovementFlags(dragFlags, swipeFlags)

        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT; //允许上下左右的拖动
        return makeMovementFlags(dragFlags, 0);
//        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {

        mAdapter.onItemMove(viewHolder,target);

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            //当滑动或者拖拽view的时候通过借口返回该viewholder
            mAdapter.onItemSelect(viewHolder);

        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if(!recyclerView.isComputingLayout()){

            mAdapter.onItemClear(viewHolder);

        }
    }

    @Override
    public boolean isLongPressDragEnabled() {


        return mAdapter.canDrag();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;

    }



}
