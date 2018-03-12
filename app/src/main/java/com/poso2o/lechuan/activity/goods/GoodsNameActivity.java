package com.poso2o.lechuan.activity.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.poso2o.lechuan.R;
import com.poso2o.lechuan.adapter.GoodsNameAdapter;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.goodsdata.GoodsNameData;
import com.poso2o.lechuan.configs.Constant;
import com.poso2o.lechuan.dialog.CommonDialog;
import com.poso2o.lechuan.tool.listener.CustomTextWatcher;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.ListUtils;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

import java.util.ArrayList;


/**
 * Created by lenovo on 2016/12/19.
 */
public class GoodsNameActivity extends BaseActivity {

    private Context context;
    private GoodsNameActivityP goodsNameActivityP;

    /**
     * 返回
     */
    private ImageView goods_name_back;

    /**
     * 写入数据
     */
    private EditText goods_name_content;
    private boolean isEdit = false;
    private final int EDIT_MESSAGE = 66;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EDIT_MESSAGE:
                    goodsNameAdapter.notifyDataSetChanged(nomalGoodsNameDatas);
                    break;
            }
        }
    };

    /**
     * 录入记录列表
     */
    private RecyclerView goods_name_recycle;
    private ArrayList<GoodsNameData> goodsNameDatas;
    private ArrayList<GoodsNameData> nomalGoodsNameDatas;
    private GoodsNameAdapter goodsNameAdapter;

    /**
     * 提交
     */
    private View goods_name_submit;
    private CommonDialog clearRecordDialog;

    /**
     * 清空记录数据
     */
    private ImageView goods_name_clear;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_name;
    }

    @Override
    public void initView() {
        context = this;
        // 返回
        goods_name_back = (ImageView) findViewById(R.id.goods_name_back);
        // 初始化记录列表
        goods_name_recycle = (RecyclerView) findViewById(R.id.goods_name_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        goods_name_recycle.setLayoutManager(linearLayoutManager);

        // 写入数据
        goods_name_content = (EditText) findViewById(R.id.goods_name_content);

        // 提交
        goods_name_submit = findViewById(R.id.goods_name_submit);

        // 清空记录数据
        goods_name_clear = (ImageView) findViewById(R.id.goods_name_clear);
        clearRecordDialog = new CommonDialog(context, R.string.dialog_clear_record);
        clearRecordDialog.setMessageColor(0xffff0000);

    }

    @Override
    public void initData() {
        goodsNameActivityP = new GoodsNameActivityP();
        // 初始化记录列表数据
        goodsNameDatas = new ArrayList<>();
        goodsNameDatas = goodsNameActivityP.getGoodsNameDatas();
        nomalDatasNameDatasFromMain();
        goodsNameAdapter = new GoodsNameAdapter(context, nomalGoodsNameDatas);
        goods_name_recycle.setAdapter(goodsNameAdapter);
    }

    /**
     * 显示容器从主容器获取数据
     */
    private void nomalDatasNameDatasFromMain() {
        if (nomalGoodsNameDatas == null) {
            nomalGoodsNameDatas = new ArrayList<>();
        } else {
            nomalGoodsNameDatas.clear();
        }

        if (goodsNameDatas.size() > 10) {
            for (int i = goodsNameDatas.size() - 1; i > goodsNameDatas.size() - 11; i--) {
                nomalGoodsNameDatas.add(goodsNameDatas.get(i));
            }
        } else {
            for (int i = goodsNameDatas.size() - 1; i > -1; i--) {
                nomalGoodsNameDatas.add(goodsNameDatas.get(i));
            }
        }
    }

    @Override
    public void initListener() {
        // 清空记录数据
        goods_name_clear.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                if (ListUtils.isNotEmpty(goodsNameDatas)) {
                    clearRecordDialog.show(new CommonDialog.OnConfirmListener() {

                        @Override
                        public void onConfirm() {
                            nomalGoodsNameDatas.clear();
                            goodsNameDatas.clear();
                            goodsNameAdapter.notifyDataSetChanged(nomalGoodsNameDatas);
                            goodsNameActivityP.clearCacheInfo();
                            clearRecordDialog.dismiss();
                        }
                    });
                } else {
                    Toast.show(context,R.string.toast_not_can_clear_record);
                }
            }
        });

        // 返回
        goods_name_back.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });

        // 写入数据
        goods_name_content.addTextChangedListener(new CustomTextWatcher() {

            @Override
            public void input(final String s, int start, int before, int count) {
                if (TextUtil.isNotEmpty(s)) {
                    goods_name_submit.setSelected(true);
                    new Thread() {

                        @Override
                        public void run() {
                            super.run();
                            visibleSearchData(s);
                        }
                    }.start();

                    isEdit = true;
                } else {
                    isEdit = false;
                    goods_name_submit.setSelected(false);
                    nomalDatasNameDatasFromMain();
                    goodsNameAdapter.notifyDataSetChanged(nomalGoodsNameDatas);
                }
            }

        });

        // 提交
        goods_name_submit.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                final String name = goods_name_content.getText().toString().trim();
                if (TextUtil.isEmpty(name)) {
                    Toast.show(context, R.string.toast_input_goods_name);
                    return;
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            GoodsNameData goodsNameData = new GoodsNameData(name);
                            goodsNameActivityP.cacheGoodsName(goodsNameDatas, goodsNameData);
                        }
                    }.start();
                }

                Intent intent = new Intent();
                intent.putExtra(Constant.NAME, name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 开启线程调用
     * 检索数据
     */
    private synchronized void visibleSearchData(String text) {
        nomalGoodsNameDatas.clear();

        for (int i = goodsNameDatas.size() - 1; i > -1; i--) {
            GoodsNameData goodsNameData = goodsNameDatas.get(i);
            int nameIndex = goodsNameData.goodsName.indexOf(text);
            if (nameIndex != -1) {
                GoodsNameData g = new GoodsNameData(goodsNameData.goodsName);
                g.goodsNameLight = goodsNameData.goodsName.substring(0, nameIndex) + getBankName((text)) + goodsNameData.goodsName.substring(nameIndex + (text).length(), goodsNameData.goodsName.length());
                nomalGoodsNameDatas.add(g);
            }
        }

        Message msg = new Message();
        msg.what = EDIT_MESSAGE;
        handler.sendMessage(msg);


    }

    private String getBankName(String editText) {
        return "<font color=\"#ff0000\">" + editText + "</font>";
    }

    /**
     * 写入数据
     *
     * @param s
     */
    public void inPut(String s) {
        goods_name_content.setText(s);
    }

    public boolean isEdit() {
        return isEdit;
    }
}
