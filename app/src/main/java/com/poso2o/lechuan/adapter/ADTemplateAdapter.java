package com.poso2o.lechuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.bean.oa.TemplateBean;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/3/13.
 *
 * 广告模板列表适配器
 */

public class ADTemplateAdapter extends BaseAdapter<ADTemplateAdapter.ADTemplateVH,TemplateBean> {

    private Context context;
    private String template_id = "";

    public ADTemplateAdapter(Context context, ArrayList data) {
        super(context, data);
        this.context = context;
    }

    public void notifyDataSetChanged(ArrayList<TemplateBean> data,String select_id) {
        template_id = select_id;
        notifyDataSetChanged(data);
    }

    @Override
    public void initItemView(ADTemplateVH holder, TemplateBean templateBean, int position) {
        Glide.with(context).load(templateBean.pic).placeholder(R.mipmap.ic_launcher).into(holder.templatePic);
        holder.templateName.setText(templateBean.template_name);
        if (templateBean.template_id.equals(template_id)){
            holder.item_main.setSelected(true);
        }else {
            holder.item_main.setSelected(false);
        }
    }

    @Override
    public ADTemplateVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_oa_template_common,parent,false);
        return new ADTemplateVH(view);
    }

    class ADTemplateVH extends BaseViewHolder{

        LinearLayout item_main;
        ImageView templatePic;
        TextView templateName;

        public ADTemplateVH(View itemView) {
            super(itemView);
            item_main = (LinearLayout) itemView.findViewById(R.id.item_main);
            templatePic = (ImageView) itemView.findViewById(R.id.iv_template_pic);
            templateName = (TextView) itemView.findViewById(R.id.tv_template_name);
        }
    }
}
