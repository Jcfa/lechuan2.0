package com.poso2o.lechuan.activity.order;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.order.OrderDTO;
import com.poso2o.lechuan.bean.system.ExpressDTO;
import com.poso2o.lechuan.bean.system.ExpressQueryDTO;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.system.SystemDataManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;

/**
 * 物流公司页面
 */
public class LogisticsCompanyActivity extends BaseActivity {
    //网络管理
    private SystemDataManager mSystemDataManager;
    //返回
    private ImageView logistics_company_back;
    //
    private LinearLayout logistics_company_groups;
    private RelativeLayout logistics_company_default_express;
    private ImageView logistics_company_item_rb;
    private TextView logistics_company_default_express_name;

    //选中的物流公司
    private ImageView choiceCheckBox;
    private ExpressDTO choiceExpressDTO = new ExpressDTO();

    //设置默认物流
    private String set_default = "0";

    @Override
    public int getLayoutResId() {
        return R.layout.activity_logistics_company;
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        logistics_company_back = (ImageView) findViewById(R.id.logistics_company_back);
        logistics_company_groups = (LinearLayout) findViewById(R.id.logistics_company_groups);
        logistics_company_default_express = (RelativeLayout) findViewById(R.id.logistics_company_default_express);
        logistics_company_item_rb = (ImageView) findViewById(R.id.logistics_company_item_rb);
        logistics_company_default_express_name = (TextView) findViewById(R.id.logistics_company_default_express_name);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        mSystemDataManager = SystemDataManager.getSystemDataManager();

        // 上一级页面传来的数据
        set_default = getIntent().getStringExtra("default");

        String default_express_id = SharedPreferencesUtils.getString("default_express_id");
        String default_express_name  = SharedPreferencesUtils.getString("default_express_name");
        if (default_express_id.isEmpty()){
            default_express_id = "shunfeng";
            default_express_name = "顺丰速递";
            choiceCheckBox = logistics_company_item_rb;
        }
        choiceExpressDTO.express_company_id = default_express_id;
        choiceExpressDTO.express_company = default_express_name;
        logistics_company_default_express_name.setText("" + choiceExpressDTO.express_company);

        loadExpress();

    }

    /**
     * 初始化监听
     */

    @Override
    public void initListener() {
        //返回
        logistics_company_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        logistics_company_default_express.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finishActivity(choiceExpressDTO);
            }
        });
    }

    /**
     * 物流公司
     */
    public void loadExpress(){
        showLoading();
        mSystemDataManager.loadExpress(activity, new IRequestCallBack<ExpressQueryDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(activity, msg);
            }

            @Override
            public void onResult(int tag, ExpressQueryDTO expressQueryDTO) {
                dismissLoading();
                refreshItem(expressQueryDTO.list);
            }
        });
    }

    /**
     * 刷新列表
     * @param list
     */
    private void refreshItem(ArrayList<ExpressDTO> list){
        // 清空列表布局
        logistics_company_groups.removeAllViews();
        // 数据不为空，填充View到列表
        if (ListUtils.isNotEmpty(list)) {
            // 第二种列表项的差异化可以在这里面完成，选择性的隐藏一些布局
            for (int i = 0; i < list.size(); i++) {

                LogisticsItemView itemView = new LogisticsItemView(activity);
                // 将View添加到列表布局里面
                logistics_company_groups.addView(itemView.getRootView());
                itemView.logistics_company_item_name.setText("" + list.get(i).express_company);
                itemView.logistics_company_item_rb.setTag(list.get(i));
                itemView.logistics_company_item_group.setTag(itemView.logistics_company_item_rb);

                if (list.get(i).express_company_id.equals(choiceExpressDTO.express_company_id)){
                    itemView.logistics_company_item_rb.setImageResource(R.mipmap.icon_ad_selected_blue_48);
                    choiceCheckBox = itemView.logistics_company_item_rb;
                    list.get(i).isChecked = true;
                }else{
                    itemView.logistics_company_item_rb.setImageResource(R.mipmap.icon_ad_select_48);
                    list.get(i).isChecked = false;
                }
            }
        }
    }

    /**
     * 物流列表
     */
    public class LogisticsItemView extends BaseView {
        View itemView;
        View logistics_company_item_group;
        TextView logistics_company_item_name;
        ImageView logistics_company_item_rb;

        public LogisticsItemView(Context context) {
            super(context);
        }

        @Override
        public View initGroupView() {
            itemView = View.inflate(context, R.layout.view_logistics_company_item, null);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return itemView;
        }

        @Override
        public void initView() {
            logistics_company_item_group = itemView.findViewById(R.id.logistics_company_item_group);
            logistics_company_item_name = (TextView) itemView.findViewById(R.id.logistics_company_item_name);
            logistics_company_item_rb = (ImageView) itemView.findViewById(R.id.logistics_company_item_rb);
        }

        @Override
        public void initData() {

        }

        @Override
        public void initListener() {
            logistics_company_item_group.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    choiceCheckBox.setImageResource(R.mipmap.icon_ad_select_48);
                    ImageView checkBox = (ImageView) v.getTag();
                    choiceCheckBox = checkBox;
                    choiceExpressDTO = (ExpressDTO) (checkBox.getTag());
                    if (choiceExpressDTO.isChecked){
                        checkBox.setImageResource(R.mipmap.icon_ad_select_48);
                    }else{
                        checkBox.setImageResource(R.mipmap.icon_ad_selected_blue_48);
                    }
                    finishActivity(choiceExpressDTO);
                }
            });
        }
    }

    /**
     * 确定返回
     * @param choiceExpressDTO
     */
    private void finishActivity(ExpressDTO choiceExpressDTO){

        if (set_default.equals("1")){
            logistics_company_default_express_name.setText("" + choiceExpressDTO.express_company);
            SharedPreferencesUtils.put("default_express_id", choiceExpressDTO.express_company_id);
            SharedPreferencesUtils.put("default_express_name", choiceExpressDTO.express_company);
        }

        //添加给第一个Activity的返回值，并设置resultCode
        Intent intent = new Intent();
        intent.putExtra("express_company_id", choiceExpressDTO.express_company_id.toString());
        intent.putExtra("express_company", choiceExpressDTO.express_company.toString());
        setResult(RESULT_OK, intent);
        finish();

    }

}
