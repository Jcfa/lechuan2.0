package com.poso2o.lechuan.layoutmanager;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.poso2o.lechuan.adapter.MainMenuAdapter;
import com.poso2o.lechuan.adapter.SettingMenuAdapter;

import java.util.Collections;


/**
 * Created by mr zhang on 2017/7/29.
 * 九宫格拖动排序删除主要实现类
 */

public class MyItemTouchCallback extends ItemTouchHelper.Callback {

    private MainMenuAdapter adapter;
    private SettingMenuAdapter menuAdapter;

    public MyItemTouchCallback(MainMenuAdapter adapter) {
        this.adapter = adapter;
    }

    public MyItemTouchCallback(SettingMenuAdapter menuAdapter) {
        this.menuAdapter = menuAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag;
        int swipeFlag;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlag = 0;
        } else {
            dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
            swipeFlag = ItemTouchHelper.END | ItemTouchHelper.START;
        }
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < toPosition) {
            if (adapter != null) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(adapter.getDataList(), i, i + 1);
                }
            } else if (menuAdapter != null) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(menuAdapter.getDataList(), i, i + 1);
                }
            }
        } else {
            if(adapter != null){
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(adapter.getDataList(), i, i - 1);
                }
            }else if(menuAdapter != null){
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(menuAdapter.getDataList(), i, i - 1);
                }
            }
        }
        if(adapter != null){
            adapter.saveMenus();
        }else if(menuAdapter != null){
//            menuAdapter.saveSettingMenu();
        }
        recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.END | direction == ItemTouchHelper.START) {
            if (adapter != null) {
                adapter.getDataList().remove(position);
                adapter.notifyItemRemoved(position);
            } else if (menuAdapter != null) {
                menuAdapter.getDataList().remove(position);
                menuAdapter.notifyItemRemoved(position);
            }
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(0);
    }
}
