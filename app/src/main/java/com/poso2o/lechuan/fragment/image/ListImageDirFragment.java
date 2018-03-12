package com.poso2o.lechuan.fragment.image;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.BaseAdapter;
import com.poso2o.lechuan.adapter.BaseViewHolder;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.image.ImageFolder;
import com.poso2o.lechuan.util.ImageLoader;

import java.util.ArrayList;

public class ListImageDirFragment extends BaseFragment {

    /**
     * 数据集合
     */
    private ArrayList<ImageFolder> data;

    /**
     * 选择监听
     */
    private OnImageDirSelected imageDirSelected;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_image_dir, container, false);
    }

    @Override
    public void initView() {
        RecyclerView recycler_dir = findView(R.id.recycler_dir);
        recycler_dir.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BaseAdapter<BaseViewHolder, ImageFolder>(getContext(), data) {

            @Override
            public void initItemView(BaseViewHolder holder, ImageFolder item, int position) {
                ((TextView) holder.getView(R.id.id_dir_item_name)).setText(item.getName().replace("/", ""));
                ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(item.getFirstImagePath(), (ImageView) holder.getView(R.id.id_dir_item_image));
                ((TextView) holder.getView(R.id.id_dir_item_count)).setText(item.getCount() + "张");
            }

            @Override
            public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new BaseViewHolder(getItemView(R.layout.item_list_dir, parent));
            }
        };
        recycler_dir.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<ImageFolder>() {

            @Override
            public void onItemClick(ImageFolder item) {
                if (imageDirSelected != null) {
                    imageDirSelected.selectFolder(item);
                }
            }
        });
    }

    public void setData(ArrayList<ImageFolder> data) {
		this.data = data;
	}

    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
        this.imageDirSelected = mImageDirSelected;
    }

    public interface OnImageDirSelected {
        void selectFolder(ImageFolder folder);
    }

    private BaseAdapter<BaseViewHolder, ImageFolder> adapter;

}
