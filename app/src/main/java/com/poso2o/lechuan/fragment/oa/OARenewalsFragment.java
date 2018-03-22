package com.poso2o.lechuan.fragment.oa;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.official.OaServiceActivity;
import com.poso2o.lechuan.activity.oa.OAHelperActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.oa.OaService;
import com.poso2o.lechuan.bean.oa.OaServiceInfo;
import com.poso2o.lechuan.dialog.ArticleNumSelectDialog;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.oa.OaServiceManager;
import com.poso2o.lechuan.util.ArithmeticUtils;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.Toast;
import com.poso2o.lechuan.view.OaPackagesGroupView;

import java.util.ArrayList;

/**
 * Created by mr zhang on 2018/2/27.
 *
 * 续费-选择更改套餐内容界面（只许升级，不可降低）
 */

public class OARenewalsFragment extends BaseFragment implements View.OnClickListener {

    private View view;

    //文章篇数
    private TextView oa_set_article_num;
    //篇数对应价格
    private TextView oa_set_article_price;

    //自主运营
    private LinearLayout self_run_layout;
    private TextView oa_self_running;
    private TextView self_run_des;
    //对应价格
    private TextView self_running_price;

    //代理运营
    private TextView oa_supper_running;
    //代理运营价格
    private TextView supper_running_price;

    //购买时长
    private LinearLayout oa_packages;
    //计算公式
    private TextView oa_total_formula;

    //总共应付
    private TextView oa_total_pay;

    //立即购买
    private TextView buy_now;

    //所有套餐信息
    private ArrayList<OaService.Item> services = new ArrayList<>();
    //默认文章篇数
    private int artNum = 3;
    //运营模式：0为自主运营，1为代运营
    private int type = 1;
    private OaPackagesGroupView groupView;
    private ArticleNumSelectDialog numSelectDialog;

    private OaServiceInfo oaServiceInfo;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(context, R.layout.fragment_oa_packages, null);
        return view;
    }

    @Override
    public void initView() {
//        oa_set_article_num = (TextView) view.findViewById(R.id.oa_set_article_num);
//        oa_set_article_price = (TextView) view.findViewById(R.id.oa_set_article_price);
//
//        self_run_layout = (LinearLayout) view.findViewById(R.id.self_run_layout);
//        oa_self_running = (TextView) view.findViewById(R.id.oa_self_running);
//        self_running_price = (TextView) view.findViewById(R.id.self_running_price);
//        self_run_des = (TextView) view.findViewById(R.id.self_run_des);
//
//        oa_supper_running = (TextView) view.findViewById(R.id.oa_supper_running);
//        supper_running_price = (TextView) view.findViewById(R.id.supper_running_price);
//
//        oa_packages = (LinearLayout) view.findViewById(R.id.oa_packages);
//
//        oa_total_pay = (TextView) view.findViewById(R.id.oa_total_pay);
//        oa_total_formula = (TextView) view.findViewById(R.id.oa_total_formula);
//
//        buy_now = (TextView) view.findViewById(R.id.buy_now);
//        buy_now.setText("确认续费");
    }

    @Override
    public void initData() {
        oaServiceInfo = ((OAHelperActivity)context).getOAServiceInfo();
        if (oaServiceInfo == null)return;
        if (oaServiceInfo.news_operation_mode == 1){
            self_run_layout.setVisibility(View.GONE);
            self_run_des.setVisibility(View.GONE);
        }
        artNum = oaServiceInfo.news_send_num;
        initPackages();
        oaService();
    }

    @Override
    public void initListener() {
        oa_set_article_num.setOnClickListener(this);

        oa_self_running.setOnClickListener(this);

        oa_supper_running.setOnClickListener(this);

        buy_now.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.oa_set_article_num:
//                clickNum();
//                break;
//            case R.id.oa_self_running:
//                changeRunning(0);
//                break;
//            case R.id.oa_supper_running:
//                changeRunning(1);
//                break;
//            case R.id.buy_now:
//                Intent toBuy = new Intent();
//                toBuy.setClass(context, OaServiceActivity.class);
//                toBuy.putExtra(OaServiceActivity.OA_RUNNING_TYPE,type);
//                toBuy.putExtra(OaServiceActivity.OA_SERVICE_DATA,groupView.getSelectItem());
//                toBuy.putExtra(OaServiceActivity.OA_SERVICE_PRICE,oa_total_pay.getText().toString());
//                getActivity().startActivityForResult(toBuy,8001);
//                break;
        }
    }

    //时长点击回调
    private OaPackagesGroupView.OnPackageChangeListener onPackageChangeListener = new OaPackagesGroupView.OnPackageChangeListener() {
        @Override
        public void onPackageChange(OaService.Item item) {
            if (supper_running_price.getVisibility() == View.VISIBLE){
                supper_running_price.setText("¥" + NumberFormatUtils.format(groupView.getSelectItem().operation_amount));
                oa_total_pay.setText("¥" + NumberFormatUtils.format(ArithmeticUtils.add(Double.parseDouble(item.amount),Double.parseDouble(item.operation_amount))));
            }else {
                oa_total_pay.setText("¥" + NumberFormatUtils.format(item.amount));
            }
            //同步服务费用信息
            if (type == 0){
                oa_total_formula.setText("可选文章（" + artNum + "篇）*一个月（" +  NumberFormatUtils.format(item.news_amount) + "）");
            }else if (type == 1){
                oa_total_formula.setText("可选文章（" + artNum + "篇）*一个月（" +  NumberFormatUtils.format(item.news_amount) + "）*" + "代运营（" +  supper_running_price.getText().toString() + "）");
            }
        }
    };

    //文章篇数点击回调
    private ArticleNumSelectDialog.OnArtNumListener onArtNumListener = new ArticleNumSelectDialog.OnArtNumListener() {
        @Override
        public void onArtNumClick(OaService.Item item) {
            artNum = Integer.parseInt(item.num);
            setPackages();
        }
    };

    //设置时长套餐
    private void initPackages(){
        groupView = new OaPackagesGroupView(context,onPackageChangeListener);
        oa_packages.addView(groupView.getRootView());
    }

    //切换运营模式
    private void changeRunning(int type){
        this.type = type;
        Drawable selectLeft = getResources().getDrawable(R.mipmap.item_select);
        Drawable noSelectLeft = getResources().getDrawable(R.mipmap.item_no_select);
        if (type == 0 && supper_running_price.getVisibility() == View.VISIBLE){
            //自主运营
            oa_total_pay.setText("¥" + NumberFormatUtils.format(groupView.getSelectItem().amount));

            self_running_price.setVisibility(View.VISIBLE);
            supper_running_price.setVisibility(View.INVISIBLE);

            oa_self_running.setCompoundDrawablesWithIntrinsicBounds(selectLeft,null,null,null);
            oa_supper_running.setCompoundDrawablesWithIntrinsicBounds(noSelectLeft,null,null,null);

            oa_total_formula.setText("可选文章（" + artNum + "篇）*一个月（" +  oa_set_article_price.getText().toString() + "）");
        }else if (type == 1 && self_running_price.getVisibility() == View.VISIBLE){
            //代理运营
            oa_total_pay.setText("¥" + NumberFormatUtils.format(ArithmeticUtils.add(Double.parseDouble(groupView.getSelectItem().amount),Double.parseDouble(groupView.getSelectItem().operation_amount))));

            self_running_price.setVisibility(View.INVISIBLE);
            supper_running_price.setVisibility(View.VISIBLE);

            oa_self_running.setCompoundDrawablesWithIntrinsicBounds(noSelectLeft,null,null,null);
            oa_supper_running.setCompoundDrawablesWithIntrinsicBounds(selectLeft,null,null,null);

            oa_total_formula.setText("可选文章（" + artNum + "篇）*一个月（" +  oa_set_article_price.getText().toString() + "）*" + "代运营（" +  supper_running_price.getText().toString() + "）");
        }
    }

    //文章篇数
    private void clickNum(){
        if (numSelectDialog != null){
            numSelectDialog.show();
            numSelectDialog.setSelected(artNum);
        }
    }

    //请求公众号服务数据
    private void oaService(){
        showLoading();
        OaServiceManager.getOaServiceManager().oaService((BaseActivity) context,"3", new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                OaService service = (OaService) result;
                if (service == null)return;
                services = service.list;
                setPackages();
                numSelectDialog = new ArticleNumSelectDialog(context,sortNumService(),onArtNumListener,artNum);
            }

            @Override
            public void onFailed(int tag, String msg) {
                dismissLoading();
                Toast.show(context,msg);
            }
        });
    }

    //根据篇数，整理时长数据
    private ArrayList<OaService.Item> sortService(){
        ArrayList<OaService.Item> sortService = new ArrayList<>();
        if (services != null){
            for (OaService.Item item : services){
                if (Integer.parseInt(item.num) == artNum){
                    sortService.add(item);
                }
            }
        }
        return sortService;
    }

    //设置时长数据
    private void setPackages(){
        groupView.setDatas(sortService());
        oa_set_article_num.setText(artNum + "");
        OaService.Item item = groupView.getSelectItem();
        oa_set_article_price.setText("¥" + NumberFormatUtils.format(item.news_amount));
        if (supper_running_price.getVisibility() == View.VISIBLE)supper_running_price.setText("¥" + NumberFormatUtils.format(item.operation_amount));
        if (supper_running_price.getVisibility() == View.VISIBLE){
            oa_total_pay.setText("¥" + NumberFormatUtils.format(ArithmeticUtils.add(Double.parseDouble(item.amount),Double.parseDouble(item.operation_amount))));
        }else {
            oa_total_pay.setText("¥" + NumberFormatUtils.format(item.amount));
        }
        oa_total_formula.setText("可选文章（" + artNum + "篇）*一个月（" +  NumberFormatUtils.format(item.news_amount) + "）*" + "代运营（" +  supper_running_price.getText().toString() + "）");
    }

    //分类篇数
    private ArrayList<OaService.Item> sortNumService(){
        ArrayList<OaService.Item> numSorts = new ArrayList<>();
        String str = "";
        for (OaService.Item item : services){
            if (!str.contains(item.num)){
                str = str + item.num + ",";
                numSorts.add(item);
            }
        }
        return numSorts;
    }
}
