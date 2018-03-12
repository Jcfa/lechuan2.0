package com.poso2o.lechuan.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.oa.OaService;

/**
 * Created by mr zhang on 2018/2/2.
 */

public class ItemOaNumView extends BaseView {

    private View view;
    private Context context;

    private TextView oa_num;
    private TextView oa_num_des;

    private OaService.Item item;

    private OnOaNumListener onArtNumListener;

    /**
     * 注意继承后 先走了初始化控件  data
     *
     * @param context
     */
    public ItemOaNumView(Context context,OaService.Item item,OnOaNumListener onArtNumListener) {
        super(context);
        this.context = context;
        this.item = item;
        this.onArtNumListener = onArtNumListener;
    }

    @Override
    public View initGroupView() {
        view = View.inflate(context, R.layout.item_oa_article_num,null);
        return view;
    }

    @Override
    public void initView() {
        oa_num = (TextView) view.findViewById(R.id.oa_num);
        oa_num_des = (TextView) view.findViewById(R.id.oa_num_des);
    }

    @Override
    public void initData() {
        oa_num.setText(item.service_name);

        view.findViewById(R.id.oa_num_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onArtNumListener.onArtNumClick(item);
            }
        });
    }

    public void setSelected(boolean b){
        oa_num.setSelected(b);
    }

    public interface OnOaNumListener{
        void onArtNumClick(OaService.Item item);
    }
}
