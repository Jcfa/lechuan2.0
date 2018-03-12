package com.poso2o.lechuan.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.poso2o.lechuan.R;
import com.poso2o.lechuan.base.BaseActivity;
import com.poso2o.lechuan.bean.poster.PosterDTO;
import com.poso2o.lechuan.manager.main.ShareManager;
import com.poso2o.lechuan.tool.listener.NoDoubleClickListener;
import com.poso2o.lechuan.tool.print.Print;
import com.poso2o.lechuan.util.TextUtil;
import com.poso2o.lechuan.util.Toast;

/**
 * 分享窗口
 * Created by Luo on 2017/1/18.
 */
public class ShareDialog extends BaseDialog {
    private Context context;
    private View view;
    private LinearLayout dialog_share_wechat, dialog_share_wxcircle, dialog_share_qzone, dialog_share_qq;//,umeng_socialize_sina;
    private TextView dialog_share_close;

    //文章内容
    private PosterDTO posterData;

    //分享管理类
    private ShareManager mShareManager;

//    //加载图片
//    private RequestManager glideRequest;

    /**
     * 回调
     */
    private OnShareListener onShareListener;

    public void show(OnShareListener onShareListener) {
        this.onShareListener = onShareListener;
        super.show();
    }

    public ShareDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void show(PosterDTO posterData, int type) {
        this.posterData = posterData;
        super.show();
        if (type == 1) {
            //直接分享到微信朋友圈
            shareWeChatCircle(1);
        }
    }

    @Override
    public View setDialogContentView() {
        return view = View.inflate(context, R.layout.dialog_share, null);
    }

    @Override
    public void initView() {
        setWindowDispalay(1, OUT_TO);

        dialog_share_close = (TextView) view.findViewById(R.id.dialog_share_close);

        dialog_share_wechat = (LinearLayout) view.findViewById(R.id.dialog_share_wechat);
        dialog_share_wxcircle = (LinearLayout) view.findViewById(R.id.dialog_share_wxcircle);
        dialog_share_qzone = (LinearLayout) view.findViewById(R.id.dialog_share_qzone);
        dialog_share_qq = (LinearLayout) view.findViewById(R.id.dialog_share_qq);
//        umeng_socialize_sina = (LinearLayout) view.findViewById(R.id.umeng_socialize_sina);

    }

    @Override
    public void initData() {
        setDialogGravity(Gravity.BOTTOM);

        //分享管理类
        mShareManager = ShareManager.getShareManager();

//        //加载图片
//        glideRequest = Glide.with(context);
    }

    @Override
    public void initListener() {
        //关闭
        dialog_share_close.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                dismiss();
            }
        });
        //微信好友
        dialog_share_wechat.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //Toast.show(context,"微信好友");
                //posterData.news_img
                shareWeChatCircle(0);
            }
        });
        //微信朋友圈
        dialog_share_wxcircle.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                //Toast.show(context,"微信朋友圈");
                //mShareManager.weixinShareWeb((BaseActivity) context, posterData.url,posterData.news_title,posterData.articles,R.mipmap.ic_launcher,true);
                shareWeChatCircle(1);
            }
        });
        //QQ空间
        dialog_share_qzone.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                mShareManager.qzoneShareImageAndText((BaseActivity) context, posterData.news_title, posterData.articles, posterData.url, posterData.news_img);
                dismiss();
            }
        });
        //QQ好友
        dialog_share_qq.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                mShareManager.qqShareImageAndText((BaseActivity) context, posterData.news_title, posterData.articles, posterData.url, posterData.news_img);
                dismiss();
            }
        });
//        //新浪微博
//        umeng_socialize_sina.setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            public void onNoDoubleClick(View v) {
//                Toast.show(context,"新浪微博");
//            }
//        });
    }

    /**
     * 直接分享到微信朋友圈
     */
    private void shareWeChatCircle(int type) {
        //Toast.show(context,"直接分享到微信朋友圈");
        loadNetImg(type);
    }

    /**
     * 加载网络图片
     */
    private void loadNetImg(final int type) {
//        ((BaseActivity) context).showLoading();
        if (posterData == null || TextUtil.isEmpty(posterData.news_img)) {
            mShareManager.weixinShareWebImage((BaseActivity) context, posterData.url, posterData.news_title, posterData.articles, R.mipmap.ic_launcher, type == 0 ? false : true);
            dismiss();
            return;
        }
        WaitDialog.showLoaddingDialog(context, "加载图片...");
        Glide.with(context)
                .load(posterData.news_img)
                .asBitmap().toBytes()//强制Glide返回一个Bitmap对象
                .priority(Priority.HIGH)
                .into(new SimpleTarget<byte[]>(50, 50) {
                    @Override
                    public void onResourceReady(byte[] bitmap, GlideAnimation<? super byte[]> glideAnimation) {
//                ((BaseActivity) context).dismissLoading();
                        WaitDialog.dismissLoaddingDialog();
                        mShareManager.weixinShareWebImageForByte((BaseActivity) context, posterData.url, posterData.news_title, posterData.articles, bitmap, type == 0 ? false : true);
                        dismiss();
                    }
                });
    }

    public interface OnShareListener {
        void onConfirm();
    }
}
