package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.MemberDetailAdapter;
import com.poso2o.lechuan.bean.member.Member;
import com.poso2o.lechuan.bean.member.MemberOrder;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2017/8/28.
 */

public class MemberDetailDialog extends BaseDialog implements View.OnClickListener{

    private View view;
    private Context context;

    //scrollview
    private ScrollView member_detail_scrollview;
    //会员头像
    private ImageView detail_member_pic;
    //会员名称
    private TextView detail_member_name;
    //会员号
    private TextView detail_member_no;
    //关闭
    private ImageView member_detail_close;
    //列表
    private RecyclerView member_order_recycler;

    //列表适配器
    private MemberDetailAdapter memberDetailAdapter;

    //是否微店
    private boolean is_online;

    public MemberDetailDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        view = View.inflate(context, R.layout.dialog_member_detail,null);
        return view;
    }

    @Override
    public void initView() {

        member_detail_scrollview = (ScrollView) view.findViewById(R.id.member_detail_scrollview);

        detail_member_pic = (ImageView) view.findViewById(R.id.detail_member_pic);

        detail_member_name = (TextView) view.findViewById(R.id.detail_member_name);

        detail_member_no = (TextView) view.findViewById(R.id.detail_member_no);

        member_detail_close = (ImageView) view.findViewById(R.id.member_detail_close);

        member_order_recycler = (RecyclerView) view.findViewById(R.id.member_order_recycler);

    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.BOTTOM);
        setWindowDispalay(1.0f, 0.7f);
        getWindow().setWindowAnimations(R.style.BottomInAnimation);
    }

    @Override
    public void initListener() {
        member_detail_close.setOnClickListener(this);
    }

    public void initDatas(boolean is_online,Member member, ArrayList<MemberOrder> memberOrders){
        this.is_online = is_online;
        if (member == null)return;
        if (is_online){
            if (!member.member_photo.equals("")) Glide.with(context).load(member.member_photo).placeholder(R.mipmap.icon_user_logo).into(detail_member_pic);
            detail_member_name.setText(member.member_name);
            detail_member_no.setText(member.member_id);
        }else {
            detail_member_name.setText(member.nick);
            detail_member_no.setText(member.uid);
        }

        memberDetailAdapter = new MemberDetailAdapter(context,memberOrders,is_online);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        member_order_recycler.setLayoutManager(linearLayoutManager);
        member_order_recycler.setAdapter(memberDetailAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.member_detail_close){
            dismiss();
        }
    }
}
