package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.activity.MainActivity;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.event.EventBean;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.bean.poster.PosterRedBagDTO;
import com.poso2o.lechuan.http.IRequestCallBack;
import com.poso2o.lechuan.manager.poster.PosterRedBagDataManager;
import com.poso2o.lechuan.tool.glide.GlideCircleTransform;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.util.SharedPreferencesUtils;
import com.poso2o.lechuan.util.Toast;

import org.greenrobot.eventbus.EventBus;

/**
 * 红包窗口
 * Created by Luo on 2017/1/18.
 */
public class RedbagDialog extends BaseDialog {

    private Context context;
    private View view;

    private PosterDTO mPosterDTO;

    //网络管理
    private PosterRedBagDataManager mPosterRedBagDataManager;
    //加载图片
    private RequestManager glideRequest;

    private ImageView redbag_dialog_close, redbag_dialog_head_img;
    private TextView redbag_dialog_head_name;

    private Button open_red_bag_btn;
    private ImageView open_red_bag_animation_iv;

    private TextView redbag_dialog_textView, redbag_dialog_textView_b, redbag_dialog_textView_c;

    private View redbag_dialog_head_oth;
    private LinearLayout redbag_dialog_user_layout;
    private LinearLayout redbag_dialog_other_layout;
    private ImageView redbag_dialog_other_imageView;
    private TextView redbag_dialog_other_tv;

    /**
     * 回调
     */
    private OnRedbagListener onRedbagListener;

    public void show(PosterDTO posterData, OnRedbagListener onRedbagListener) {
        this.mPosterDTO = posterData;
        this.onRedbagListener = onRedbagListener;
        super.show();
    }

    public RedbagDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_red_bag, null);
    }

    @Override
    public void initView() {
        setWindowDispalay(OUT_TO, OUT_TO);//0.8f
        redbag_dialog_close = (ImageView) view.findViewById(R.id.redbag_dialog_close);
        redbag_dialog_head_img = (ImageView) view.findViewById(R.id.redbag_dialog_head_img);
        redbag_dialog_head_name = (TextView) view.findViewById(R.id.redbag_dialog_head_name);

        open_red_bag_btn = (Button) view.findViewById(R.id.open_red_bag_btn);
        open_red_bag_animation_iv = (ImageView) view.findViewById(R.id.open_red_bag_animation_iv);

        redbag_dialog_textView = (TextView) view.findViewById(R.id.redbag_dialog_textView);
        redbag_dialog_textView_b = (TextView) view.findViewById(R.id.redbag_dialog_textView_b);
        redbag_dialog_textView_c = (TextView) view.findViewById(R.id.redbag_dialog_textView_c);

        redbag_dialog_head_oth = (View) view.findViewById(R.id.redbag_dialog_head_oth);
        redbag_dialog_user_layout = (LinearLayout) view.findViewById(R.id.redbag_dialog_user_layout);
        redbag_dialog_other_layout = (LinearLayout) view.findViewById(R.id.redbag_dialog_other_layout);
        redbag_dialog_other_imageView = (ImageView) view.findViewById(R.id.redbag_dialog_other_imageView);
        redbag_dialog_other_tv = (TextView) view.findViewById(R.id.redbag_dialog_other_tv);


    }

    @Override
    public void initData() {
        mPosterRedBagDataManager = PosterRedBagDataManager.getRedBagDataManager();
        //加载图片
        glideRequest = Glide.with(context);
        glideRequest.load("" + mPosterDTO.logo).transform(new GlideCircleTransform(context)).into(redbag_dialog_head_img);
        redbag_dialog_head_name.setText("" + mPosterDTO.nick);
    }

    @Override
    public void initListener() {
        redbag_dialog_close.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
        //开红包
        open_red_bag_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                addPosterRedBag();
            }
        });
        //查看红包详情
        redbag_dialog_other_tv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                context.startActivity(new Intent(context, MainActivity.class).putExtra(MainActivity.KEY_SKIP, MainActivity.TO_BALANCE));
            }
        });
    }

    /**
     * 抢红包
     */
    private void addPosterRedBag() {

        open_red_bag_btn.setVisibility(View.GONE);
        open_red_bag_animation_iv.setVisibility(View.VISIBLE);

        open_red_bag_animation_iv.setImageResource(R.drawable.lottery_animlist);
        final AnimationDrawable animationDrawable = (AnimationDrawable) open_red_bag_animation_iv.getDrawable();
        animationDrawable.start();

        mPosterRedBagDataManager.addPosterRedBag((BaseActivity) context, "" + mPosterDTO.news_id, new IRequestCallBack<PosterRedBagDTO>() {
            @Override
            public void onFailed(int tag, String msg) {
                Toast.show(context, msg);
                animationDrawable.stop();
                open_red_bag_btn.setVisibility(View.VISIBLE);
                open_red_bag_animation_iv.setVisibility(View.GONE);
            }

            @Override
            public void onResult(int tag, PosterRedBagDTO posterRedBagDTO) {
                animationDrawable.stop();
                //open_red_bag_btn.setVisibility(View.VISIBLE);
                //open_red_bag_animation_iv.setVisibility(View.GONE);
                EventBus.getDefault().post(new EventBean(EventBean.REDBAG_STATUS_ID));
                redbag_dialog_user_layout.setBackgroundResource(R.mipmap.ic_red_money_open);
                redbag_dialog_head_oth.setVisibility(View.VISIBLE);
                redbag_dialog_head_name.setTextColor(Color.parseColor("#5e5e5e"));
                redbag_dialog_head_name.setText(redbag_dialog_head_name.getText().toString() + "的红包");
                redbag_dialog_textView.setVisibility(View.GONE);
                redbag_dialog_textView_b.setTextColor(Color.parseColor("#5e5e5e"));
                redbag_dialog_textView_b.setTextSize(16);
                redbag_dialog_textView_c.setVisibility(View.VISIBLE);
                redbag_dialog_textView_c.setText("¥" + posterRedBagDTO.amount);
                open_red_bag_btn.setVisibility(View.GONE);
                open_red_bag_animation_iv.setVisibility(View.GONE);
                redbag_dialog_other_layout.setVisibility(View.GONE);
                redbag_dialog_other_imageView.setVisibility(View.GONE);
                redbag_dialog_other_tv.setVisibility(View.VISIBLE);

                if (onRedbagListener != null) {
                    onRedbagListener.onConfirm();
                }

            }
        });
    }

    public interface OnRedbagListener {
        void onConfirm();
    }
}
