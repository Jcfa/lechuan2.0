package com.poso2o.lechuan.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.articledata.ArticleData;

/**
 * Created by mr zhang on 2017/10/26.
 */

public class ArtHistoryItemView extends BaseView implements View.OnClickListener{

    private View view;
    private Context context;

    //根布局
    private RelativeLayout art_history_item;
    //图片
    private ImageView item_history_list_pic;
    //标题
    private TextView art_history_list_title;
    //阅读量
    private TextView art_history_list_read;
    //赞
    private TextView art_history_list_prise;
    //删除
    private TextView art_history_list_del;

    //文章数据
    private ArticleData articleData;

    /**
     * 注意继承后 先走了初始化控件  data
     *
     * @param context
     */
    public ArtHistoryItemView(Context context ,ArticleData articleData) {
        super(context);
        this.context = context;
        this.articleData = articleData;
    }

    @Override
    public View initGroupView() {
        view = View.inflate(context, R.layout.view_art_history_item,null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return view;
    }

    @Override
    public void initView() {
        art_history_item = (RelativeLayout) view.findViewById(R.id.art_history_item);
        item_history_list_pic = (ImageView) view.findViewById(R.id.item_history_list_pic);
        art_history_list_title = (TextView) view.findViewById(R.id.art_history_list_title);
        art_history_list_read = (TextView) view.findViewById(R.id.art_history_list_read);
        art_history_list_prise = (TextView) view.findViewById(R.id.art_history_list_prise);
        art_history_list_del = (TextView) view.findViewById(R.id.art_history_list_del);
    }

    @Override
    public void initData() {
        initMyListener();

        if (articleData == null)return;
        if (articleData.pic != null && !articleData.pic.equals("")) Glide.with(context).load(articleData.pic).into(item_history_list_pic);
        art_history_list_title.setText(articleData.title);
        art_history_list_read.setText("阅读 " + articleData.readnums);
        art_history_list_prise.setText("赞 " + articleData.collectnums);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.art_history_item:
                break;
            case R.id.art_history_list_del:
                if (onArtHistoryListListener != null)onArtHistoryListListener.onArtDelClick(articleData);
                break;
        }
    }

    private void initMyListener(){
        art_history_item.setOnClickListener(this);
        art_history_list_del.setOnClickListener(this);
    }

    private OnArtHistoryListListener onArtHistoryListListener;
    public interface OnArtHistoryListListener{
        void onArtDelClick(ArticleData articleData);
    }
    public void setOnArtHistoryListListener(OnArtHistoryListListener onArtHistoryListListener){
        this.onArtHistoryListListener = onArtHistoryListListener;
    }
}
