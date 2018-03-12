package com.poso2o.lechuan.view;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.goods.EditGoodsActivity;
import com.poso2o.lechuan.activity.wshop.WEditGoodsActivity;
import com.poso2o.lechuan.base.BaseView;
import com.poso2o.lechuan.bean.goodsdata.GoodsSpec;
import com.poso2o.lechuan.tool.listener.CustomTextWatcher;
import com.poso2o.lechuan.tool.listener.MoneyTextWatcher;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.NumberFormatUtils;
import com.poso2o.lechuan.util.NumberUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import static android.view.View.GONE;

/**
 * Created by mr zhang on 2017/12/20.
 */

public class WSpecViewHolder {
    private WEditGoodsActivity context;

    public View view;

    /**
     * 规格
     */
    public LinearLayout spec_item_spec_group;
    public TextView spec_item_spec;
    public TextView spec_item_unit;

    /**
     * 售价
     */
    private EditText spec_item_price;
    private View spec_item_price_group;

    /**
     * 成本
     */
    private EditText spec_item_cost;
    private View spec_item_cost_group;

    /**
     * 条码
     */
    private ImageView spec_item_to_bar_code;
    private EditText spec_item_barcode;

    /**
     * 库存
     */
    private EditText spec_item_stock;

    /**
     * 规格数据
     */
    private GoodsSpec spec;

    /**
     * 是否微店
     */
    private boolean isVdian;

    public WSpecViewHolder(WEditGoodsActivity context, GoodsSpec spec, boolean isVdian) {
        this.context = context;
        this.spec = spec;
        this.isVdian = isVdian;
        this.view = View.inflate(context, R.layout.item_edit_goods_spec_view, null);
        initView();
        initListener();
    }

    public void setCode(String code) {
        spec.spec_bar_code = code;
        spec_item_barcode.setText(TextUtil.trimToEmpty(code));
    }

    private void initView() {
        // 规格
        spec_item_spec_group = (LinearLayout) view.findViewById(R.id.spec_item_spec_group);
        spec_item_spec = (TextView) view.findViewById(R.id.spec_item_spec);
        spec_item_unit = (TextView) view.findViewById(R.id.spec_item_unit);

        // 价格
        spec_item_price = (EditText) view.findViewById(R.id.spec_item_price);
        spec_item_price_group = view.findViewById(R.id.spec_item_price_group);
        spec_item_cost = (EditText) view.findViewById(R.id.spec_item_cost);
        spec_item_cost_group = view.findViewById(R.id.spec_item_cost_group);

        // 条码
        spec_item_to_bar_code = (ImageView) view.findViewById(R.id.spec_item_to_bar_code);
        spec_item_barcode = (EditText) view.findViewById(R.id.spec_item_barcode);

        // 库存
        spec_item_stock = (EditText) view.findViewById(R.id.spec_item_stock);

        if (spec != null) {
            // 判断当前用户是否是主账号，否则隐藏成本价
//            if (!sharedPreferencesUtil.getString(UserInfoMemory.USER_ID).equals(sharedPreferencesUtil.getString(UserInfoMemory.SHOP_ID))) {
//                spec_item_cost_layout.setVisibility(GONE);
//            } else {
//                spec_item_cost.setText(NumberFormatUtils.format(spec.spec_cost));
//            }
            spec_item_cost.setText(NumberFormatUtils.format(spec.spec_cost));
            spec_item_spec.setText(spec.goods_spec_name);
            if (TextUtil.isNotEmpty(spec.goods_auxiliary_unit)) {
                spec_item_unit.setText("(" + spec.goods_auxiliary_unit_packingrate + spec.goods_unit + ")");
            }
            spec_item_price.setText(NumberFormatUtils.format(spec.spec_price));
            spec_item_stock.setText(NumberFormatUtils.formatToInteger(spec.spec_number));
            spec_item_barcode.setText(TextUtil.trimToEmpty(spec.spec_bar_code));
        }

        if (!isVdian) {
            spec_item_price_group.setVisibility(GONE);
            spec_item_cost_group.setVisibility(GONE);
        }
    }

    private void initListener() {
        // 售价
        spec_item_price.addTextChangedListener(new MoneyTextWatcher(spec_item_price) {

            @Override
            public void input(String s) {
                spec.spec_price = NumberUtils.toDouble(s);
            }
        });

        // 成本
        spec_item_cost.addTextChangedListener(new MoneyTextWatcher(spec_item_cost) {

            @Override
            public void input(String s) {
                spec.spec_cost = NumberUtils.toDouble(s);
            }
        });

        // 条码
        spec_item_to_bar_code.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                context.toScanCodeActivity(WSpecViewHolder.this);
            }
        });

        spec_item_barcode.addTextChangedListener(new CustomTextWatcher() {

            @Override
            public void input(String s, int start, int before, int count) {
                if (spec_item_barcode.getText().toString().trim().length() > 20) {
                    spec_item_barcode.setText(spec.spec_bar_code);
                    Toast.show(context, "条码不能超过20个字符");
                } else {
                    spec.spec_bar_code = spec_item_barcode.getText().toString().trim();
                }
            }
        });

        spec_item_stock.addTextChangedListener(new CustomTextWatcher() {

            @Override
            public void input(String s, int start, int before, int count) {
                spec.spec_number = NumberUtils.toInt(s);
                context.calculateStock();
            }
        });
    }
}
