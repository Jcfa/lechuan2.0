package com.poso2o.lechuan.activity.vdian;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.wopenaccountdata.OpenStandBean;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.wopenaccountmanager.EmpowermentManager;
import com.poso2o.lechuan.util.Toast;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * 开通授权界面
 * <p>
 * Created by Administrator on 2018/3/13 0013.
 */
public class EmpowermentActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 有公众号
     */
    private LinearLayout empower_have_oa;
    private ImageView empower_have_oa_select;
    private TextView empower_have_oa_title, empower_have_oa_content;

    /**
     * 无公众号
     */
    private LinearLayout empower_not_oa;
    private ImageView empower_not_oa_select;
    private TextView empower_not_oa_title, empower_not_oa_content;

    /**
     * 开通公众号
     */
    private View empower_apply_oa;
    private TextView empower_apply_oa_hint;

    /**
     * 公众号授权
     */
    private View empower_oa_empower;
    private TextView empower_oa_empower_tag, empower_oa_empower_hint;

    /**
     * 服务订购
     */
    private View empower_ordering;
    private TextView empower_ordering_tag;

    /**
     * 开通状态
     */
    private int state;

    /**
     * 联系方式
     */
    private String attn, mobile;

    /**
     * 服务信息
     */
    private String service_name, amount, payment_time;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_empowerment;
    }

    @Override
    protected void initView() {
        empower_have_oa = findView(R.id.empower_have_oa);
        empower_have_oa_select = findView(R.id.empower_have_oa_select);
        empower_have_oa_title = findView(R.id.empower_have_oa_title);
        empower_have_oa_content = findView(R.id.empower_have_oa_content);

        empower_not_oa = findView(R.id.empower_not_oa);
        empower_not_oa_select = findView(R.id.empower_not_oa_select);
        empower_not_oa_title = findView(R.id.empower_not_oa_title);
        empower_not_oa_content = findView(R.id.empower_not_oa_content);

        empower_apply_oa = findView(R.id.empower_apply_oa);
        empower_apply_oa_hint = findView(R.id.empower_apply_oa_hint);

        empower_oa_empower = findView(R.id.empower_oa_empower);
        empower_oa_empower_tag = findView(R.id.empower_oa_empower_tag);
        empower_oa_empower_hint = findView(R.id.empower_oa_empower_hint);

        empower_ordering = findView(R.id.empower_ordering);
        empower_ordering_tag = findView(R.id.empower_ordering_tag);
    }

    @Override
    protected void initData() {
        setTitle("开通授权申请");

        showLoading();
        loadStates();
    }

    public void loadStates() {
        // 获取开通状态
        EmpowermentManager.getInstance().OpenStateDate(this, new IRequestCallBack() {
            @Override
            public void onResult(int tag, Object result) {
                dismissLoading();
                Gson gson = new Gson();
                OpenStandBean osb = gson.fromJson(result.toString(), OpenStandBean.class);
                attn = osb.getAttn();
                mobile = osb.getMobile();
                service_name = osb.getService_name();
                amount = osb.getAmount();
                payment_time = osb.getPayment_time();
                if (osb.getState() == OpenStandBean.WAIT_OPEN_STATE) {//去开通
                    // 点击无公众号
                    empower_have_oa.setBackgroundResource(R.drawable.gray_stroke_bg);
                    empower_not_oa.setBackgroundResource(R.drawable.green_stroke_bg);
                    empower_have_oa_select.setImageResource(R.drawable.icon_not_select);
                    empower_not_oa_select.setImageResource(R.drawable.selected);

                    empower_not_oa_title.setTextColor(getResources().getColor(R.color.textGreen));
                    empower_not_oa_content.setTextColor(getResources().getColor(R.color.placeholder_70));

                    empower_have_oa_title.setTextColor(getResources().getColor(R.color.textGray));
                    empower_have_oa_content.setTextColor(getResources().getColor(R.color.placeholder_30));

                    empower_apply_oa.setVisibility(View.VISIBLE);
                    empower_oa_empower_tag.setText("步骤二：");
                    empower_ordering_tag.setText("步骤三：");
                    empower_oa_empower.setBackgroundColor(getResources().getColor(R.color.common_background));
                    empower_oa_empower.setEnabled(false);
                    empower_apply_oa_hint.setText("去开通");
                    findView(R.id.iv_checked_state).setVisibility(View.GONE);
                } else if (osb.getState() == OpenStandBean.PAY_COSE_STATE) {//已经缴费、待开通
                    empower_apply_oa_hint.setText("已缴费、待开通");
                    findView(R.id.iv_checked_state).setVisibility(View.VISIBLE);
                } else if (osb.getState() == OpenStandBean.HAS_OPENED_STATE) {//已经开通
                    empower_apply_oa_hint.setText("已开通");
                    findView(R.id.iv_checked_state).setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context, msg);
                dismissLoading();
            }
        });
    }

    @Override
    protected void initListener() {
        empower_have_oa.setOnClickListener(this);
        empower_not_oa.setOnClickListener(this);
        empower_ordering.setOnClickListener(this);
        empower_oa_empower.setOnClickListener(this);
        empower_apply_oa.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.empower_have_oa:
                // 点击有公众号
                empower_have_oa.setBackgroundResource(R.drawable.green_stroke_bg);
                empower_not_oa.setBackgroundResource(R.drawable.gray_stroke_bg);
                empower_have_oa_select.setImageResource(R.drawable.selected);
                empower_not_oa_select.setImageResource(R.drawable.icon_not_select);
                empower_have_oa_title.setTextColor(getResources().getColor(R.color.textGreen));
                empower_have_oa_content.setTextColor(getResources().getColor(R.color.placeholder_70));

                empower_not_oa_title.setTextColor(getResources().getColor(R.color.textGray));
                empower_not_oa_content.setTextColor(getResources().getColor(R.color.placeholder_30));
                empower_apply_oa.setVisibility(View.GONE);
                empower_oa_empower_tag.setText("步骤一：");
                empower_ordering_tag.setText("步骤二：");
                empower_oa_empower.setBackgroundResource(R.drawable.selector_white);
                empower_oa_empower.setEnabled(true);
                break;

            case R.id.empower_not_oa:
                // 点击无公众号
                empower_have_oa.setBackgroundResource(R.drawable.gray_stroke_bg);
                empower_not_oa.setBackgroundResource(R.drawable.green_stroke_bg);
                empower_have_oa_select.setImageResource(R.drawable.icon_not_select);
                empower_not_oa_select.setImageResource(R.drawable.selected);

                empower_not_oa_title.setTextColor(getResources().getColor(R.color.textGreen));
                empower_not_oa_content.setTextColor(getResources().getColor(R.color.placeholder_70));

                empower_have_oa_title.setTextColor(getResources().getColor(R.color.textGray));
                empower_have_oa_content.setTextColor(getResources().getColor(R.color.placeholder_30));

                empower_apply_oa.setVisibility(View.VISIBLE);
                empower_oa_empower_tag.setText("步骤二：");
                empower_ordering_tag.setText("步骤三：");
                empower_oa_empower.setBackgroundColor(getResources().getColor(R.color.common_background));
                empower_oa_empower.setEnabled(false);
                break;

            case R.id.empower_ordering:
                // 进入服务订购/试用
                Intent i = new Intent();
                i.setClass(EmpowermentActivity.this, ServiceOrderingActivity.class);
                startActivity(i);
                break;

            case R.id.empower_oa_empower:
                // 进入授权说明
                Intent ia = new Intent();
                ia.setClass(EmpowermentActivity.this, AuthorizationActivity.class);
                startActivity(ia);
                break;

            case R.id.empower_apply_oa:
                if (state == 0) {
                    // 进入未开通状态公众号
                    Intent in = new Intent();
                    in.putExtra("attn", attn);
                    in.putExtra("mobile", mobile);
                    in.setClass(EmpowermentActivity.this, ApplyOAActivity.class);
                    startActivity(in);
                } else if (state == 1) {
                    // 进入已缴费，待开通状态公众号
                    Intent in = new Intent();
                    in.putExtra("amount", amount);
                    in.putExtra("service_name", service_name);
                    in.putExtra("payment_time", payment_time);
                    in.putExtra("attn", attn);
                    in.putExtra("mobile", mobile);
                    in.setClass(EmpowermentActivity.this, ApplyStatusActivity.class);
                    startActivity(in);
                } else if (state == 2) {
                    // 已开通状态
                    empower_apply_oa.setClickable(false);
                }
                break;
        }
    }
}
