package com.poso2o.lechuan.fragment.redbag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.topup.TopUpActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.base.BaseFragment;
import com.poso2o.lechuan.bean.redbag.Redbag;
import com.poso2o.lechuan.tool.edit.KeyboardUtils;
import com.poso2o.lechuan.tool.listener.CustomTextWatcher;
import com.poso2o.lechuan.tool.listener.MoneyTextWatcher;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.ScreenInfo;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import java.math.BigDecimal;

import static com.poso2o.lechuan.util.SharedPreferencesUtils.KEY_USER_RED_ENVELOPES_AMOUNT;

public class AddRedbagFragment extends BaseFragment implements OnClickListener {

    private static final int REQUEST_TOP_UP = 2;
    
    /**
     * 回调变量
     */
    private Callback callback;
    
    /**
     * 红包横条
     */
    private TextView add_redbag_bar;
    
    /**
     * 金额
     */
    private TextView add_redbag_money_tag;
    
    /**
     * 总金额
     */
    private TextView add_redbag_total;
    
    /**
     * 模式描述文本
     */
    private TextView add_redbag_describe;
    
    /**
     * 切换模式
     */
    private TextView add_redbag_switch_mode;
    
    /**
     * 可用余额
     */
    private TextView add_redbag_balance;
    
    /**
     * 数量
     */
    private EditText add_redbag_num;
    
    /**
     * 金额
     */
    private EditText add_redbag_money;
    
    /**
     * 确认并发布
     */
    private Button add_redbag_confirm;
    
    /**
     * 红包数据
     */
    private Redbag redbag;
    
    /**
     * 是否可发布
     */
    public boolean isRelease = false;

    @Override
    public View initGroupView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_redbag, container, false);
    }

    @Override
    public void initView() {
        initWindow(view);
        add_redbag_confirm = findView(R.id.add_redbag_confirm);
        add_redbag_bar = findView(R.id.add_redbag_bar);
        add_redbag_money_tag = findView(R.id.add_redbag_money_tag);
        add_redbag_balance = findView(R.id.add_redbag_balance);
        add_redbag_total = findView(R.id.add_redbag_total);
        add_redbag_switch_mode = findView(R.id.add_redbag_switch_mode);
        add_redbag_describe = findView(R.id.add_redbag_describe);
        add_redbag_num = findView(R.id.add_redbag_num);
        add_redbag_money = findView(R.id.add_redbag_money);
    }

    @Override
    public void initData() {
        add_redbag_balance.setText(Float.toString(SharedPreferencesUtils.getFloat(KEY_USER_RED_ENVELOPES_AMOUNT)));
        add_redbag_num.setText(Integer.toString(redbag.number));
        add_redbag_money.setText(NumberFormatUtils.format(redbag.oneMoney));
    }

    @Override
    public void initListener() {
        findView(R.id.add_redbag_top_up).setOnClickListener(this);
        add_redbag_bar.setOnClickListener(this);
        add_redbag_switch_mode.setOnClickListener(this);
        if (isRelease) {
            add_redbag_confirm.setOnClickListener(this);
        } else {
            add_redbag_confirm.setBackgroundResource(R.drawable.shape_gray_btn);
        }

        add_redbag_num.addTextChangedListener(new CustomTextWatcher() {

            @Override
            public void input(String s, int start, int before, int count) {
                if (NumberUtils.toInt(s) > 999) {
                    add_redbag_num.setText("999");
                    Toast.show(context, "一次最多只能发999个红包哦");
                    return;
                }
                if (s.length() > 0) {
                    redbag.number = Integer.parseInt(s);
                } else {
                    redbag.number = 0;
                }
                calculateMoney(add_redbag_num);
            }
        });

        add_redbag_money.addTextChangedListener(new MoneyTextWatcher(add_redbag_money) {

            @Override
            public void input(String s) {
                redbag.oneMoney = new BigDecimal(s);
                calculateMoney(add_redbag_money);
            }
        });
    }

    /**
     * 计算红包总金额
     * 
     * @author Zheng Jie Dong
     * @param et 
     * @date 2016-10-31
     */
    protected void calculateMoney(EditText et) {
        if (redbag.isRandom()) {// 随机红包
            redbag.money = redbag.oneMoney;
            add_redbag_total.setVisibility(View.INVISIBLE);
        } else {// 固定金额
            add_redbag_total.setVisibility(View.VISIBLE);
            // 计算总金额
            redbag.money = NumberUtils.multiply(redbag.oneMoney, new BigDecimal(redbag.number));
        }
        add_redbag_total.setText(NumberFormatUtils.format(redbag.money));
    }

    private void initWindow(View view) {
        LinearLayout layout_root = (LinearLayout) view.findViewById(R.id.layout_root);
        int title_height = getResources().getDimensionPixelSize(R.dimen.title_height);
        layout_root.setPadding(0, ScreenInfo.STATUS_BAR_HEIGHT + title_height, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.iv_back:// 返回
        	hideFragment(true);
        	break;
        	
        case R.id.add_redbag_bar:// 顶部红包横条
//            if (redbag.number > 1) {
//                // 单个红包金额必须大于1分钱
//                if (redbag.money.doubleValue() * 100 < redbag.number) {
//                    Toast.show(context, R.string.toast_one_greater_than_penny);
//                    return;
//                }
//                if (callback != null) {
//                    callback.callback(redbag, false);
//                }
//            }
            KeyboardUtils.hideSoftInput((BaseActivity) context);
            hideFragment(true);
            break;
            
        case R.id.add_redbag_top_up:// 充值横条
            startActivityForResult(TopUpActivity.class, REQUEST_TOP_UP);
            break;
            
        case R.id.add_redbag_switch_mode:// 切换红包模式
            if (redbag.isRandom()) {
                // 切换为固定金额红包
                add_redbag_describe.setText(R.string.moments_welfare_money_orderliness_describe);
                add_redbag_switch_mode.setText(R.string.update_random_money);
                redbag.setRandom(false);
                if (NumberUtils.isNotZero(redbag.money)) {
                    BigDecimal oneMoney = NumberUtils.divide(redbag.money, new BigDecimal(redbag.number));
                    add_redbag_money.setText(NumberFormatUtils.format(oneMoney));
                }
                add_redbag_money_tag.setText(R.string.one_money);
                add_redbag_money_tag.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            } else {
                // 切换为随机金额红包
                add_redbag_describe.setText(R.string.moments_welfare_money_random_describe);
                add_redbag_switch_mode.setText(R.string.update_orderliness_money);
                redbag.setRandom(true);
                add_redbag_money_tag.setText(R.string.total_money);
                add_redbag_money_tag.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_pin_tag, 0);
                add_redbag_money.setText(NumberFormatUtils.format(redbag.money));
            }
            break;
            
        case R.id.add_redbag_confirm:
            confirm();
        	break;
        }
    }

    /**
     * 确认
     *
     * @return 是否添加成功
     */
    public boolean confirm() {
        BigDecimal amount = new BigDecimal(SharedPreferencesUtils.getFloat(KEY_USER_RED_ENVELOPES_AMOUNT));
        // 判断红包金额是否大于总金额
        if (redbag.money.doubleValue() > amount.doubleValue() || redbag.oneMoney.doubleValue() > amount.doubleValue()) {
        	Toast.show(context, R.string.toast_balance_insufficient);
            return false;
        }
        if (redbag.number == 0) {
            Toast.show(context, R.string.toast_redbag_number_unable_zero);
            return false;
        }
        // 单个红包金额必须大于1分钱
        if (redbag.money.doubleValue() * 100 < redbag.number) {
            Toast.show(context, R.string.toast_one_greater_than_penny);
            return false;
        }
        if (callback.callback(redbag, true)) {
            hideFragment(true);
        }
        KeyboardUtils.hideSoftInput((BaseActivity) context);
        return true;
    }

    public void setRedMoney(Redbag redbag) {
        if (redbag != null) {
            this.redbag = redbag;
        } else {
            this.redbag = new Redbag();
        }
    }
    
    public void setIsRelease(boolean isRelease) {
        this.isRelease = isRelease;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        /**
         * 回调函数
         *
         * @param redbag
         * @param isSend
         * @return 是否关闭界面
         */
        boolean callback(Redbag redbag, boolean isSend);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TOP_UP) {
            add_redbag_balance.setText(Float.toString(SharedPreferencesUtils.getFloat(KEY_USER_RED_ENVELOPES_AMOUNT)));
    		getActivity().setResult(Activity.RESULT_OK);
    	}
    }

    public void refreshRedEnvelopesAmount() {
        add_redbag_balance.setText(Float.toString(SharedPreferencesUtils.getFloat(KEY_USER_RED_ENVELOPES_AMOUNT)));
    }
}
